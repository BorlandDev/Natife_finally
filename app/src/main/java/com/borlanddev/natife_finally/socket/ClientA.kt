package com.borlanddev.natife_finally.socket

import android.util.Log
import com.borlanddev.natife_finally.helpers.TCP_PORT
import com.borlanddev.natife_finally.helpers.UDP_PORT
import com.borlanddev.natife_finally.helpers.USERNAME_A
import com.borlanddev.natife_finally.model.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import model.PingDto
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class ClientA : CoroutineScope {

    private var myID = ""
    private var anotherID = ""
    private var myIP = ""
    private val gson = Gson()
    private val timeout = 20_000
    private var socket: Socket? = null
    private var response: String? = null
    private var users: List<User> = listOf()
    override val coroutineContext = (Job() + Dispatchers.IO)
    private val scope = CoroutineScope(coroutineContext)
    private val pinPong = CoroutineScope(Job() + Dispatchers.Default)

    suspend fun getToConnection() {
        scope.launch(Dispatchers.IO) {
            val udpSocket = DatagramSocket()
            udpSocket.soTimeout = timeout

            try {
                val message = ByteArray(1024)
                val packet = DatagramPacket(
                    message, message.size,
                    InetAddress.getByName("255.255.255.255"), UDP_PORT
                )
                val message2 = ByteArray(1024)
                val answer = DatagramPacket(
                    message2, message2.size
                )
                while (myIP.isEmpty()) {
                    try {
                        udpSocket.send(packet)
                        udpSocket.receive(answer)
                        myIP = answer.address.hostName
                        Log.d("Alice_clientIP", myIP)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                tcpConnect(myIP)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                udpSocket.close()
            }
        }
    }

    private suspend fun tcpConnect(clientIP: String) {
        scope.launch(Dispatchers.IO) {
            socket = Socket(clientIP, TCP_PORT)
            socket?.soTimeout = timeout

            val reader = BufferedReader(InputStreamReader(socket?.getInputStream()))
            val writer = PrintWriter(OutputStreamWriter(socket?.getOutputStream()))

            try {
                scope.launch(Dispatchers.IO) {
                    while (true) {
                        response = reader.readLine()
                        Log.d("ALice_response", response.toString())
                        delay(5_000)

                        if (response != null) {
                            val result = gson.fromJson(response, BaseDto::class.java)

                            when (result.action) {
                                BaseDto.Action.CONNECTED -> {
                                    myID = gson.fromJson(result.payload, ID::class.java).id

                                    sendConnect(writer, reader)
                                    delay(3_000)

                                    getUsers(writer)
                                }

                                BaseDto.Action.USERS_RECEIVED -> {
                                    users =
                                        gson.fromJson(
                                            result.payload,
                                            UsersReceivedDto::class.java
                                        ).users

                                    anotherID = users[0].id
                                    sendMessage(writer, "Hi, Bob)")
                                }

                                BaseDto.Action.PONG -> {
                                    pinPong.cancel()
                                }

                                BaseDto.Action.SEND_MESSAGE -> {
                                    Log.d(
                                        "ALice_SEND_MESSAGE",
                                        result.payload
                                    )
                                }
                                BaseDto.Action.NEW_MESSAGE -> {
                                    val person = gson.fromJson(
                                        result.payload,
                                        MessageDto::class.java
                                    ).from.name

                                    val message = gson.fromJson(
                                        result.payload,
                                        MessageDto::class.java
                                    ).message

                                    Log.d(
                                        "Alice_NEW_MESSAGE",
                                        "Message from: $person - $message"
                                    )
                                }
                                BaseDto.Action.DISCONNECT -> {
                                    disconnect(writer, reader)
                                    Log.d(
                                        "ALice_DISCONNECT",
                                        "SEND_DISCONNECT"
                                    )
                                }
                                else -> Log.d("ALice_null", "")
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun sendConnect(writer: PrintWriter, reader: BufferedReader) {
        try {
            scope.launch(Dispatchers.IO) {
                val dto = gson.toJson(
                    BaseDto(
                        BaseDto.Action.CONNECT,
                        gson.toJson(ConnectDto(myID, USERNAME_A))
                    )
                )
                writer.println(dto)
                sendPing(writer, reader)
                writer.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private suspend fun sendPing(writer: PrintWriter, reader: BufferedReader) {
        try {
            scope.launch(Dispatchers.IO) {
                val dto = gson.toJson(
                    BaseDto(
                        BaseDto.Action.PING,
                        gson.toJson(PingDto(myID))
                    )
                )
                while (true) {
                    writer.println(dto)
                    writer.flush()
                    delay(7_000)

                    pinPong.launch {
                        delay(10_000)
                        disconnect(writer, reader)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getUsers(writer: PrintWriter) {
        try {
            scope.launch(Dispatchers.IO) {
                val dto = gson.toJson(
                    BaseDto(
                        BaseDto.Action.GET_USERS,
                        gson.toJson(GetUsersDto(myID))
                    )
                )
                writer.println(dto)
                writer.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendMessage(writer: PrintWriter, message: String) {
        try {
            scope.launch(Dispatchers.IO) {
                val dto = gson.toJson(
                    BaseDto(
                        BaseDto.Action.SEND_MESSAGE,
                        gson.toJson(SendMessageDto(myID, anotherID, message))
                    )
                )
                writer.println(dto)
                writer.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun disconnect(
        writer: PrintWriter,
        reader: BufferedReader
    ) {
        writer.flush()
        reader.close()
        socket?.close()
        scope.cancel()
    }
}



