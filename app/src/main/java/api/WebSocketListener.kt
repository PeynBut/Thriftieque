package api

interface WebSocketListener {
    fun onConnected()
    fun onMessageReceived(message: String)
    fun onDisconnected()
    fun onError(ex: Exception?)
}
