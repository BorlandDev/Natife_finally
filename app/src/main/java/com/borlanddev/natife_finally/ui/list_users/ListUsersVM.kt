package com.borlanddev.natife_finally.ui.list_users

import androidx.lifecycle.ViewModel
import com.borlanddev.data.consts.DEFAULT_NAME_PREFS
import com.borlanddev.domain.socket.Client
import com.borlanddev.domain.storage.Prefs
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
        prefs.putUsername(DEFAULT_NAME_PREFS)
        client.disconnect()
    }
}
