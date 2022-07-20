package com.borlanddev.domain.socket

import com.borlanddev.domain.model.MessageDto
import com.borlanddev.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

interface Client {

    val singedIn: SharedFlow<Boolean>

    val listUsers: SharedFlow<List<User>>

    val newMessage: SharedFlow<MessageDto>

    fun connect(name: String)

    fun getClientId(): String

    fun getUsers()

    fun sendMessage(message: String, recipientID: String)

    fun disconnect()
}