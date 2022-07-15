package com.borlanddev.natife_finally.ui.list_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.model.User
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListUsersVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    private val listUsersVMFlow = client.listUsers
    val listUsersVM: SharedFlow<List<User>> = listUsersVMFlow

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                client.getUsers()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.deleteUsername()
            client.disconnect()
        }
    }


}
