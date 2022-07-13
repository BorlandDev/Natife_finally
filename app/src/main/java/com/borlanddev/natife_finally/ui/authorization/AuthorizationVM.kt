package com.borlanddev.natife_finally.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.helpers.APP_PREFERENCES
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthorizationVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    private var username = ""
    var isSigned = flow { emit(false) }

    fun provideUsername() {
        viewModelScope.launch {
            authorization(username)
        }
    }

    fun authorization(username: String) {
        prefs.preferences.edit().putString(APP_PREFERENCES, username).apply()
        viewModelScope.launch {
            try {
                client.getToConnection(username)

                isSigned.collect {
                    while (!client.singedIn) {
                        isSigned = flow { emit(true) }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun isSignedIn(): Boolean {
        username = prefs.preferences.getString(
            APP_PREFERENCES, ""
        ).toString()

        return username.isNotEmpty()
    }
}