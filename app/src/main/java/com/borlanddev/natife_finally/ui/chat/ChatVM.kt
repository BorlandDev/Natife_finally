package com.borlanddev.natife_finally.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.data.socket.Client
import com.borlanddev.data.model.MessageDto
import com.borlanddev.data.model.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.borlanddev.data.storage.Prefs

class ChatVM @Inject constructor(
    private val recipientID: String,
    private val client: Client,
    private val prefs: Prefs
) : ViewModel() {

    private var list = listOf<MessageDto>()
    private val clientId = client.getClientId()
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

    fun checkSender(messageDto: MessageDto): Boolean = messageDto.from.id == clientId

    private fun receiveClientMessages(clientMessage: String) {
        val messageDto = MessageDto(
            User(
                clientId,
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