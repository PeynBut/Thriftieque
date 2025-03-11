package com.rendonapp.thriftique


import api.WebSocketListener
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class ChatWebSocket(serverUri: URI, private val listener: WebSocketListener) : WebSocketClient(serverUri) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("Connected to WebSocket")
        listener.onConnected()
    }

    override fun onMessage(message: String?) {
        message?.let {
            println("Received: $it")
            listener.onMessageReceived(it)
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("WebSocket closed: $reason")
        listener.onDisconnected()
    }

    override fun onError(ex: Exception?) {
        println("Error: ${ex?.message}")
        listener.onError(ex)
    }
}
