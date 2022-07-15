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

    private var savedName = ""
    val singedInVM = client.singedIn

    fun authorization(username: String = savedName) {
        if (isSignedIn()) {
            savedName = prefs.getUsername()
        } else {
            prefs.putUsername(username)
        }

        try {
            client.getToConnection(username)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun isSignedIn(): Boolean = prefs.getUsername().isNotEmpty()
}