package com.borlanddev.natife_finally.ui.list_users

import androidx.lifecycle.ViewModel
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListUsersVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    val users = client.listUsers

    init {
        getUsers()
    }

    fun getUsers() {
        try {
            client.getUsers()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun logOut() {
        prefs.deleteUsername()
        client.disconnect()
    }

}
