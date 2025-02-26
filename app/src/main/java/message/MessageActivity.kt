package message

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.WebSocketListener
import com.google.android.material.appbar.MaterialToolbar
import com.rendonapp.thriftique.R
import com.rendonapp.thriftique.ChatWebSocket
import org.json.JSONObject
import java.net.URI

class MessageActivity : AppCompatActivity(), WebSocketListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messages: MutableList<Message> = mutableListOf()
    private lateinit var chatWebSocket: ChatWebSocket

    private val currentUser = "User1"
    private val chatPartner = "User2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messageactivity)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)

        topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recyclerView = findViewById(R.id.recyclerViewMessages)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<Button>(R.id.btnSend)

        messageAdapter = MessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        val serverUri = URI("ws://192.168.100.184:8080")

        chatWebSocket = ChatWebSocket(serverUri, this)
        chatWebSocket.connect()

        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(currentUser, chatPartner, messageText)
                etMessage.text.clear()
            }
        }
    }

    private fun sendMessage(sender: String, receiver: String, messageText: String) {
        val messageJson = JSONObject()
        messageJson.put("sender", sender)
        messageJson.put("receiver", receiver)
        messageJson.put("message", messageText)

        if (chatWebSocket.isOpen) {
            chatWebSocket.send(messageJson.toString())
        } else {
            runOnUiThread {
                showSystemMessage("Error: Not connected to server")
            }
        }
    }

    override fun onConnected() {
        runOnUiThread {
            showSystemMessage("Connected to server")
        }
    }

    override fun onMessageReceived(message: String) {
        runOnUiThread {
            try {
                val jsonMessage = JSONObject(message)
                val sender = jsonMessage.optString("sender", "Unknown")
                val text = jsonMessage.optString("message", "")

                if (text.isNotEmpty()) {
                    messages.add(Message(sender, text))
                    messageAdapter.notifyItemInserted(messages.size - 1)
                    recyclerView.scrollToPosition(messages.size - 1)
                }
            } catch (e: Exception) {
                showSystemMessage("Error parsing message: ${e.message}")
            }
        }
    }

    override fun onDisconnected() {
        runOnUiThread {
            showSystemMessage("Disconnected from server")
        }
    }

    override fun onError(ex: Exception?) {
        runOnUiThread {
            showSystemMessage("Error: ${ex?.message}")
        }
    }

    private fun showSystemMessage(message: String) {
        messages.add(Message("System", message))
        messageAdapter.notifyItemInserted(messages.size - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        chatWebSocket.close()
    }
}
