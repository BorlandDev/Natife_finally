package com.borlanddev.natife_finally.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.model.MessageDto
import com.borlanddev.natife_finally.model.User
import com.borlanddev.natife_finally.socket.Client
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatVM @Inject constructor(
    private val recipientID: String,
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    private val list = mutableListOf<MessageDto>()
    private val listMessageFlow = MutableSharedFlow<MutableList<MessageDto>>()
    val listMessage: SharedFlow<List<MessageDto>> = listMessageFlow

    val clientId = client.clientId

    init {
        receiveRecipientMessages()
    }

    fun sendMessage(message: String) {
        client.sendMessage(message, recipientID)
        receiveClientMessages(message)
    }

    private fun receiveClientMessages(clientMessage: String) {
        val messageDto = MessageDto(
            User(
                clientId,
                prefs.getUsername()
            ), clientMessage
        )
        addMessage(messageDto)
    }

    private fun receiveRecipientMessages() {
        viewModelScope.launch {
            client.newMessage.collect {
                addMessage(it)
            }
        }
    }

    private fun addMessage(messageDto: MessageDto) {
        viewModelScope.launch {
            list.add(messageDto)
            listMessageFlow.emit(list)
        }
    }
}