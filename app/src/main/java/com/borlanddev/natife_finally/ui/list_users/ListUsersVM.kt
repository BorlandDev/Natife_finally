package com.borlanddev.natife_finally.ui.list_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.helpers.APP_PREFERENCES
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.model.User
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListUsersVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    var listUsers: Flow<List<User>> = flow {
        emit(
            listOf(
                User("3", "Ivan"),
                User("3", "Maxim")
            )
        )
    }
    private var username = ""

    init {
        if (isSignedIn()) {
            client.getToConnection(username)
        }
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {

        while (client.users.toList().isEmpty()) {
            client.getUsers()
        }
            try {
                client.users.collect{
                   listUsers = flow {
                        emit(listOf(User("32", "Maaaaaaax")))
                    }
                }

                /*
                client.users.collect {
                    while (client.users.toList().isEmpty())
                        listUsers = flow { emit(it) }
                }

                 */
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun isSignedIn(): Boolean {
        username = prefs.preferences.getString(
            APP_PREFERENCES, ""
        ).toString()

        return username.isNotEmpty()
    }

    fun logOut() {
        prefs.preferences.edit().putString(APP_PREFERENCES, "").apply()
        client.disconnect()
    }
}