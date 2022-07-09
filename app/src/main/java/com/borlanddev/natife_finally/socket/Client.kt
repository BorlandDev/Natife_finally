package com.borlanddev.natife_finally.socket

import android.util.Log
import com.borlanddev.natife_finally.helpers.CLIENT_NAME
import com.borlanddev.natife_finally.helpers.TCP_PORT
import com.borlanddev.natife_finally.helpers.UDP_PORT
import com.borlanddev.natife_finally.model.BaseDto
import com.borlanddev.natife_finally.model.ConnectDto
import com.borlanddev.natife_finally.model.ConnectedDto
import com.borlanddev.natife_finally.model.PongDto
import com.google.gson.Gson
import kotlinx.coroutines.*
import model.PingDto
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class Client {

    private var clientIP: String = ""
    private val timeout = 20_000
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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
            val id: String

            try {
                id = reader.readLine()
                Log.d("AAA_ServerID", id)

                val ping = gson.toJson(BaseDto(BaseDto.Action.PING, gson.toJson(PingDto(id))))
                val pong = gson.toJson(BaseDto(BaseDto.Action.PONG, gson.toJson(PongDto(id))))

                sendCONNECT(id, writer)
                sendCONNECTED(id, writer)
                sendPingToServer(writer, ping)
                listenerAnswerFromServer(reader)

                writer.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    private fun sendCONNECT(id: String, writer: PrintWriter) {
        scope.launch(Dispatchers.IO) {
            val connect = Gson().toJson(
                BaseDto(
                    BaseDto.Action.CONNECT,
                    Gson().toJson(ConnectDto(id, CLIENT_NAME))
                )
            )
            try {
                writer.println(connect)
                writer.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun sendCONNECTED(id: String, writer: PrintWriter) {
        scope.launch(Dispatchers.IO) {
            val connect = Gson().toJson(
                BaseDto(
                    BaseDto.Action.CONNECTED,
                    Gson().toJson(ConnectedDto(id))
                )
            )
            try {
                writer.println(connect)
                writer.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun sendPingToServer(writer: PrintWriter, ping: String) {
        scope.launch(Dispatchers.IO) {
            while (true) {
                writer.println(ping)
                delay(8_000)
            }
        }
    }

    private suspend fun listenerAnswerFromServer(reader: BufferedReader) {
        scope.launch(Dispatchers.IO) {
            while (true) {
                val msg: String? = reader.readLine()
                Log.d("AAA_msg", msg ?: "nothing")
                delay(5_000)
            }
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



