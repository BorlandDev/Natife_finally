package com.borlanddev.natife_finally.ui.chat

import androidx.lifecycle.ViewModel
import com.borlanddev.natife_finally.socket.Client
import javax.inject.Inject

class ChatVM @Inject constructor(
    private val recipientID: String,
    private val client: Client
) : ViewModel() {

    val newMessage = client.newMessage
    val clientId = client.clientId

    fun sendMessage(message: String) {
        client.sendMessage(message, recipientID)
    }
}