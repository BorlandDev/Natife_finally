package com.borlanddev.natife_finally.ui.list_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.helpers.APP_PREFERENCES
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.model.User
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListUsersVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    init {
        getUsers()
    }

    var stateFlow = MutableStateFlow(listOf<User>())

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                while (true) {
                    client.getUsers()
                    client.stateFlow.collect {
                        stateFlow.value = it
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun logOut() {
        prefs.preferences.edit().putString(APP_PREFERENCES, "").apply()
        client.disconnect()
    }


}
