package com.borlanddev.natife_finally.ui.list_users

import androidx.lifecycle.ViewModel
import com.borlanddev.natife_finally.helpers.APP_PREFERENCES
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListUsersVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    private var username = ""

    init {
        if (isSignedIn()) {
            client.getToConnection(username)
        }
    }

    fun logOut() {
        prefs.preferences.edit().putString(APP_PREFERENCES, "").apply()
        client.disconnect()
    }

    private fun isSignedIn(): Boolean {
        username = prefs.preferences.getString(
            APP_PREFERENCES, ""
        ).toString()

        return username.isNotEmpty()
    }
}