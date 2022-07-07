package com.borlanddev.natife_finally.socket

import android.util.Log
import com.borlanddev.natife_finally.helpers.TCP_PORT
import com.borlanddev.natife_finally.helpers.UDP_PORT
import com.borlanddev.natife_finally.model.BaseDto
import com.borlanddev.natife_finally.model.PongDto
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import model.PingDto
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class Client {

    private val socket = DatagramSocket()
    private var clientIP: String = ""

    fun getToConnection(coroutineScope: CoroutineScope) {
        val message = ByteArray(1024)
        val packet = DatagramPacket(
            message, message.size,
            InetAddress.getByName("255.255.255.255"), UDP_PORT
        )
        val message2 = ByteArray(1024)
        val answer = DatagramPacket(
            message2, message2.size
        )
        while (clientIP == "") {
            try {
                socket.send(packet)
                socket.receive(answer)
                clientIP = answer.address.hostName
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        tcpConnect(clientIP, coroutineScope)
        socket.close()
    }

    private fun tcpConnect(clientIP: String, coroutineScope: CoroutineScope) {
        try {
            val gson = Gson()
            val socket = Socket(clientIP, TCP_PORT)
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val writer = PrintWriter(OutputStreamWriter(socket.getOutputStream()))

            val id = reader.readLine()
            Log.d("AAA", id)
            //////////////////////////////////////////////////////////////////////

            val ping = gson.toJson(BaseDto(BaseDto.Action.PING , gson.toJson(PingDto(id))))
            val pong = gson.toJson(BaseDto(BaseDto.Action.PONG, gson.toJson(PongDto(id))))

            coroutineScope.launch(Dispatchers.IO) {
                flow {
                    while (true) {
                        emit(writer.println(ping))
                        delay(10_000)
                    }
                }
            }

            coroutineScope.launch(Dispatchers.IO) {
                flow {
                    while (true) {
                        emit(reader.readLine())
                    }
                }.collect {
                   Log.d("AAA", "${it?.toString()}")
                }
            }


            /*
            val connectDto = gson.toJson(ConnectDto(id, CLIENT_NAME))
            val baseDto = gson.toJson(BaseDto(BaseDto.Action.CONNECT, connectDto))
            writer.println(baseDto)

            // get users
            try {
                writer.println(
                    gson.toJson(BaseDto(BaseDto.Action.GET_USERS, gson.toJson(GetUsersDto(id))))
                )
                val listUsers = reader.readLine()

                for (i in listUsers) {
                    Log.d("AAALis", i.toString())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
             */
            writer.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            socket.close()
        }
    }
}

