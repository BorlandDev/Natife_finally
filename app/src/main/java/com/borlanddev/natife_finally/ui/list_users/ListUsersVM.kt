package com.borlanddev.natife_finally.ui.list_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.model.User
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListUsersVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    private val listUsersVMFlow = MutableSharedFlow<List<User>>()
    val listUsersVM = listUsersVMFlow

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                client.getUsers()

                client.listUsers.collect {
                    listUsersVMFlow.emit(it)
                }
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
