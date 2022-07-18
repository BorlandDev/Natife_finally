package com.borlanddev.natife_finally.socket

import android.util.Log
import com.borlanddev.natife_finally.helpers.*
import com.borlanddev.natife_finally.model.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import model.PingDto
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Client @Inject constructor() {

    private val gson = Gson()
    private var username = DEFAULT_NAME_PREFS
    private var clientID = ""
    val clientId = clientID
    private var pingPong: Job? = null
    private var socket: Socket? = null
    private var response: String? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null
    private var connect = MutableStateFlow(false)
    private val singedInFlow = MutableSharedFlow<Boolean>()
    val singedIn: SharedFlow<Boolean> = singedInFlow
    private val listUsersFlow = MutableSharedFlow<List<User>>()
    val listUsers: SharedFlow<List<User>> = listUsersFlow
    private val newMessageFlow = MutableSharedFlow<MessageDto>()
    val newMessage: SharedFlow<MessageDto> = newMessageFlow
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun connect(name: String) {
        scope.launch(Dispatchers.IO) {
            var serverIP = ""
            val udpSocket = DatagramSocket()
            udpSocket.soTimeout = TO_DISCONNECT_TIME_OUT
            username = name

            try {
                val message = ByteArray(1024)
                val packet = DatagramPacket(
                    message, message.size,
                    InetAddress.getByName(LOCAL_HOST), UDP_PORT
                )
                val message2 = ByteArray(1024)
                val answer = DatagramPacket(
                    message2, message2.size
                )
                while (serverIP.isEmpty()) {
                    try {
                        udpSocket.send(packet)
                        udpSocket.receive(answer)
                        serverIP = answer.address.hostName
                        Log.d("Client_clientIP", serverIP)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                tcpConnect(serverIP)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                udpSocket.close()
            }
        }
    }

    private fun tcpConnect(serverIP: String) {
        socket = Socket(serverIP, TCP_PORT)
        socket?.soTimeout = TO_DISCONNECT_TIME_OUT

        reader = BufferedReader(InputStreamReader(socket?.getInputStream()))
        writer = PrintWriter(OutputStreamWriter(socket?.getOutputStream()))

        connect.value = true
        listeningServerResponse()
    }


    private fun listeningServerResponse() {
        scope.launch(Dispatchers.IO) {
            while (connect.value) {
                try {
                    response = reader?.readLine()
                    Log.d("Client_response", response.toString())

                    if (response != null) {
                        val result = gson.fromJson(response, BaseDto::class.java)

                        when (result.action) {
                            BaseDto.Action.CONNECTED -> {
                                clientID = gson.fromJson(
                                    result.payload, ConnectedDto::class.java
                                ).id

                                sendPing()
                                sendConnect(username)
                            }

                            BaseDto.Action.USERS_RECEIVED -> {
                                listUsersFlow.emit(
                                    gson.fromJson(
                                        result.payload,
                                        UsersReceivedDto::class.java
                                    ).users
                                )
                            }

                            BaseDto.Action.PONG -> {
                                pingPong?.cancel()
                            }

                            BaseDto.Action.NEW_MESSAGE -> {
                                newMessageFlow.emit(
                                    gson.fromJson(
                                        result.payload,
                                        MessageDto::class.java
                                    )
                                )
                            }
                            else -> Log.d("Client_null", "")
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun sendPing() {
        scope.launch(Dispatchers.IO) {
            val dto = gson.toJson(
                BaseDto(
                    BaseDto.Action.PING,
                    gson.toJson(PingDto(clientID))
                )
            )
            while (connect.value) {
                try {
                    pingPong = launch {
                        delay(PING_PONG_TIME_OUT)
                        disconnect()
                    }
                    writer?.println(dto)
                    writer?.flush()
                    delay(PING_TIME_OUT)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun sendConnect(username: String) {
        scope.launch(Dispatchers.IO) {
            val dto = gson.toJson(
                BaseDto(
                    BaseDto.Action.CONNECT,
                    gson.toJson(ConnectDto(clientID, username))
                )
            )
            try {
                writer?.println(dto)
                writer?.flush()

                singedInFlow.emit(true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getUsers() {
        scope.launch(Dispatchers.IO) {
            try {
                val dto = gson.toJson(
                    BaseDto(
                        BaseDto.Action.GET_USERS,
                        gson.toJson(GetUsersDto(clientID))
                    )
                )
                writer?.println(dto)
                writer?.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(message: String, recipientID: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val dto = gson.toJson(
                    BaseDto(
                        BaseDto.Action.SEND_MESSAGE,
                        gson.toJson(SendMessageDto(clientID, recipientID, message))
                    )
                )
                writer?.println(dto)
                writer?.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        scope.launch(Dispatchers.IO) {
            try {
                val dto = gson.toJson(
                    BaseDto(
                        BaseDto.Action.DISCONNECT,
                        gson.toJson(DisconnectDto(clientID, 0))
                    )
                )
                writer?.println(dto)
                writer?.flush()
                reader?.close()
                socket?.close()
                writer?.close()
                connect.value = false
                singedInFlow.emit(false)
                scope.coroutineContext.job.cancelChildren()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}



