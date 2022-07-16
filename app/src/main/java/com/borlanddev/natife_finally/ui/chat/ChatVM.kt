package com.borlanddev.natife_finally.ui.chat

import androidx.lifecycle.ViewModel
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatVM @Inject constructor(
    private val client: Client
) : ViewModel() {

    val message = client.message

    fun sendMessage(message: String, recipientID: String) {
        client.sendMessage(message, recipientID)
    }

}