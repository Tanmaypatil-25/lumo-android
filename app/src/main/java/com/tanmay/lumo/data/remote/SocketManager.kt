package com.tanmay.lumo.data.remote

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import com.google.gson.Gson
import com.tanmay.lumo.data.model.Message

object SocketManager {

    private const val TAG = "LumoSocket"

    private var socket: Socket? = null

    private val gson = Gson()

    private var onNewMessageListener: ((Message) -> Unit)? = null

    private var onMessageSeenListener: ((String) -> Unit)? = null

    fun setOnMessageSeenListener(
        listener: (String) -> Unit
    ) {
        onMessageSeenListener = listener
    }

    private var onOnlineUsersListener: ((List<String>) -> Unit)? = null

    private var latestOnlineUsers: List<String> = emptyList()

    fun setOnOnlineUsersListener(
        listener: (List<String>) -> Unit
    ) {
        onOnlineUsersListener = listener

        if (latestOnlineUsers.isNotEmpty()) {
            listener(latestOnlineUsers)
        }
    }

    private var onTypingListener: ((String) -> Unit)? = null
    private var onStopTypingListener: ((String) -> Unit)? = null

    fun setOnTypingListener(listener: (String) -> Unit) {
        onTypingListener = listener
    }

    fun setOnStopTypingListener(listener: (String) -> Unit) {
        onStopTypingListener = listener
    }

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

            socket?.on("newMessage") { args ->

                try {
                    val data = args.firstOrNull()

                    if (data != null) {
                        val message = gson.fromJson(
                            data.toString(),
                            Message::class.java
                        )

                        Log.d(
                            TAG,
                            "New message received: ${message.text}"
                        )

                        onNewMessageListener?.invoke(message)
                    }

                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "Failed to parse new message",
                        e
                    )
                }
            }

            socket?.on("messageSeen") { args ->

                try {
                    val data = args.firstOrNull() as? org.json.JSONObject

                    if (data != null) {
                        val messageId = data.getString("messageId")

                        Log.d(TAG, "Message seen: $messageId")

                        onMessageSeenListener?.invoke(messageId)
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to process messageSeen", e)
                }
            }

            socket?.on("getOnlineUsers") { args ->

                try {
                    val data = args.firstOrNull() as? org.json.JSONArray

                    if (data != null) {

                        val onlineUsers = mutableListOf<String>()

                        for (i in 0 until data.length()) {
                            onlineUsers.add(data.getString(i))
                        }

                        Log.d(
                            TAG,
                            "Online users: $onlineUsers"
                        )

                        latestOnlineUsers = onlineUsers

                        onOnlineUsersListener?.invoke(onlineUsers)
                    }

                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "Failed to process online users",
                        e
                    )
                }
            }

            socket?.on("typing") { args ->
                try {
                    val data = args.firstOrNull() as? org.json.JSONObject

                    if (data != null) {
                        val senderId = data.getString("senderId")

                        onTypingListener?.invoke(senderId)
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to process typing event", e)
                }
            }

            socket?.on("stopTyping") { args ->
                try {
                    val data = args.firstOrNull() as? org.json.JSONObject

                    if (data != null) {
                        val senderId = data.getString("senderId")

                        onStopTypingListener?.invoke(senderId)
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to process stopTyping event", e)
                }
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

    fun setOnNewMessageListener(
        listener: (Message) -> Unit
    ) {
        onNewMessageListener = listener
    }

    fun emitTyping(receiverId: String) {
        socket?.emit("typing", receiverId)
    }

    fun emitStopTyping(receiverId: String) {
        socket?.emit("stopTyping", receiverId)
    }
}
