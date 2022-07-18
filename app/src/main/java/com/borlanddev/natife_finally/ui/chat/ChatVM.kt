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

    private var list = listOf<MessageDto>()
    private val username: String by lazy { prefs.getUsername() }

    private val listMessageFlow = MutableSharedFlow<List<MessageDto>>()
    val listMessage: SharedFlow<List<MessageDto>> = listMessageFlow

    init {
        receiveRecipientMessages()
    }

    fun sendMessage(message: String) {
        client.sendMessage(message, recipientID)
        receiveClientMessages(message)
    }

    fun checkSender(messageDto: MessageDto): Boolean = messageDto.from.id == client.getClientId()


    private fun receiveClientMessages(clientMessage: String) {
        val messageDto = MessageDto(
            User(
                client.getClientId(),
                username
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
            list = list + messageDto
            listMessageFlow.emit(list)
        }
    }
}