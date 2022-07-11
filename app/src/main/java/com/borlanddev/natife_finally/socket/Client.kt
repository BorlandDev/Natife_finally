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

    private var clientIP = ""
    private var id = ""
    private var response: String? = null
    private val timeout = 20_000
    override val coroutineContext = (Job() + Dispatchers.IO)
    private val scope = CoroutineScope(coroutineContext)

    suspend fun getToConnection() {
        scope.launch(Dispatchers.IO) {
            val socket = DatagramSocket()
            socket.soTimeout = timeout

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
                        socket.send(packet)
                        socket.receive(answer)
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
                socket.close()
            }
        }
    }

    private suspend fun tcpConnect(clientIP: String) {
        scope.launch(Dispatchers.IO) {
            val gson = Gson()
            val socket = Socket(clientIP, TCP_PORT)
            socket.soTimeout = timeout

            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val writer = PrintWriter(OutputStreamWriter(socket.getOutputStream()))

            try {
                scope.launch(Dispatchers.IO) {
                    while (true) {
                        response = reader.readLine()
                        //Log.d("AAA_response", response.toString())
                        delay(5_000)

                        if (response != null) {
                            val result = gson.fromJson(response, BaseDto::class.java)

                            when (result.action) {
                                BaseDto.Action.CONNECTED -> {
                                    val str = gson.fromJson(result.payload, ID::class.java)
                                    id = str.id
                                    sendCONNECT(writer)
                                    delay(3_000)

                                    getUsers(writer)
                                }

                                BaseDto.Action.USERS_RECEIVED -> {
                                    val users =
                                        gson.fromJson(result.payload, UsersReceivedDto::class.java)
                                    Log.d("AAA_LIST_USERS", users.users.toString())

                                    for (i in users.users) {
                                        Log.d("AAA_USER", "$i")
                                    }
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
                                    disconnect(socket, writer, reader)
                                    Log.d(
                                        "AAA_DISCONNECT",
                                        "SEND_DISCONNECT"
                                    )
                                }
                                else -> Log.d("AAA_null", "null")
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun sendCONNECT(writer: PrintWriter) {
        try {
            scope.launch(Dispatchers.IO) {
                val connect = Gson().toJson(
                    BaseDto(
                        BaseDto.Action.CONNECT,
                        Gson().toJson(ConnectDto(id, CLIENT_NAME))
                    )
                )
                writer.println(connect)
                sendPing(writer)
                writer.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private suspend fun sendPing( writer: PrintWriter) {
        try {
            scope.launch(Dispatchers.IO) {
                val ping = Gson().toJson(
                    BaseDto(
                        BaseDto.Action.PING,
                        Gson().toJson(PingDto(id))
                    )
                )
                while (true) {
                    writer.println(ping)
                    writer.flush()
                    delay(7_000)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getUsers(writer: PrintWriter) {
        try {
            scope.launch(Dispatchers.IO) {
                val dto = Gson().toJson(
                    BaseDto(
                        BaseDto.Action.GET_USERS,
                        Gson().toJson(GetUsersDto(id))
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
        socket: Socket,
        writer: PrintWriter,
        reader: BufferedReader
    ) {
        writer.flush()
        reader.close()
        socket.close()
        scope.cancel()
    }
}



