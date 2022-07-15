package com.borlanddev.natife_finally.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthorizationVM @Inject constructor(
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    private var savedName = ""
    private val singedInVMFlow = MutableSharedFlow<Boolean>()
    val singedInVM = singedInVMFlow

    fun authorization(username: String = savedName) {

        // Если мы авторизованы , берем имя из префов
        if (isSignedIn()) {
            savedName = prefs.getUsername()
        }

        // Если не авториованы , берем введенное имя и кладем в префы
        prefs.putUsername(username)

        // Затем коннектимся к серверу с введенным именем
        viewModelScope.launch(Dispatchers.IO) {
            try {
                client.getToConnection(username)

                client.singedIn.collect {
                    singedInVM.emit(it)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun isSignedIn(): Boolean = prefs.getUsername().isNotEmpty()
}