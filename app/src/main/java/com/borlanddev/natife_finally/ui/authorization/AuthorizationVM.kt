package com.borlanddev.natife_finally.ui.authorization

import androidx.lifecycle.ViewModel
import com.borlanddev.natife_finally.helpers.DEFAULT_NAME_PREFS
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthorizationVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    val singedIn = client.singedIn

    fun authorization(username: String) {
        try {
            if (!isSignedIn()) {
                prefs.putUsername(username)
            }
            client.connect(username)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getSavedName(): String = prefs.getUsername()
    fun isSignedIn(): Boolean = prefs.getUsername() != DEFAULT_NAME_PREFS
}