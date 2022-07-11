package com.borlanddev.natife_finally.socket

import android.util.Log
import com.borlanddev.natife_finally.helpers.CLIENT_NAME
import com.borlanddev.natife_finally.helpers.TCP_PORT
import com.borlanddev.natife_finally.helpers.UDP_PORT
import com.borlanddev.natife_finally.model.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import model.PingDto
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class Client : CoroutineScope {

    private var id = ""
    private var clientIP = ""
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
                while (clientIP.isEmpty()) {
                    try {
                        udpSocket.send(packet)
                        udpSocket.receive(answer)
                        clientIP = answer.address.hostName
                        Log.d("AAA_clientIP", clientIP)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                tcpConnect(clientIP)
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
                        Log.d("AAA_response", response.toString())
                        delay(5_000)

                        if (response != null) {
                            val result = gson.fromJson(response, BaseDto::class.java)

                            when (result.action) {
                                BaseDto.Action.CONNECTED -> {
                                    id = gson.fromJson(result.payload, ID::class.java).id

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


                                    Log.d("AAA_LIST_USERS", users.size.toString())
                                }

                                BaseDto.Action.PONG -> {
                                    pinPong.cancel()
                                }

                                BaseDto.Action.SEND_MESSAGE -> {
                                    Log.d(
                                        "AAA_SEND_MESSAGE",
                                        "SEND_MESSAGE"
                                    )
                                }
                                BaseDto.Action.NEW_MESSAGE -> {
                                    Log.d(
                                        "AAA_NEW_MESSAGE",
                                        "SEND_NEW_MESSAGE"
                                    )
                                }
                                BaseDto.Action.DISCONNECT -> {
                                    disconnect(writer, reader)
                                    Log.d(
                                        "AAA_DISCONNECT",
                                        "SEND_DISCONNECT"
                                    )
                                }
                                else -> Log.d("AAA_null", "")
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
                val connect = gson.toJson(
                    BaseDto(
                        BaseDto.Action.CONNECT,
                        gson.toJson(ConnectDto(id, CLIENT_NAME))
                    )
                )
                writer.println(connect)
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
                val ping = gson.toJson(
                    BaseDto(
                        BaseDto.Action.PING,
                        gson.toJson(PingDto(id))
                    )
                )
                while (true) {
                    writer.println(ping)
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
                        gson.toJson(GetUsersDto(id))
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



