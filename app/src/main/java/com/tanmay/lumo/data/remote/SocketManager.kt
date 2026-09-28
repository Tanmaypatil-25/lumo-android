package com.tanmay.lumo.data.remote

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

object SocketManager {

    private const val TAG = "LumoSocket"

    private var socket: Socket? = null

    fun connect(
        serverUrl: String,
        token: String
    ) {

        if (socket?.connected() == true) {
            return
        }

        try {

            val options = IO.Options().apply {

                auth = mapOf(
                    "token" to token
                )
            }

            socket = IO.socket(
                serverUrl,
                options
            )

            socket?.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "Socket connected")
            }

            socket?.on(Socket.EVENT_DISCONNECT) {
                Log.d(TAG, "Socket disconnected")
            }

            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e(
                    TAG,
                    "Socket connection error: ${args.firstOrNull()}"
                )
            }

            socket?.connect()

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Failed to initialize socket",
                e
            )
        }
    }

    fun disconnect() {

        socket?.disconnect()
        socket?.off()

        socket = null

        Log.d(TAG, "Socket cleared")
    }

    fun isConnected(): Boolean {
        return socket?.connected() == true
    }
}