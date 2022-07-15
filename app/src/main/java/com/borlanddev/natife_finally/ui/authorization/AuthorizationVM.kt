package com.borlanddev.natife_finally.ui.authorization

import androidx.lifecycle.ViewModel
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

    val singedInVM = client.singedIn

    fun authorization(username: String = "") {
        try {
            if (isSignedIn()) {
                val savedName = prefs.getUsername()
                client.getToConnection(savedName)
            } else {
                prefs.putUsername(username)
                client.getToConnection(username)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun isSignedIn(): Boolean = prefs.getUsername().isNotEmpty()
}