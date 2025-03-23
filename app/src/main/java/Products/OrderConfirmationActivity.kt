package Products

import Order.Order
import OrderedProductAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rendonapp.thriftique.Homepage
import com.rendonapp.thriftique.R

class OrderConfirmationActivity : AppCompatActivity() {

    private lateinit var orderedProducts: List<Product>
    private lateinit var adapter: OrderedProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        // ✅ Find views using findViewById
        val tvConfirmationMessage = findViewById<TextView>(R.id.tvConfirmationMessage)
        val tvPaymentDetails = findViewById<TextView>(R.id.tvPaymentDetails)
        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)

        // ✅ Retrieve ordered products from intent
        orderedProducts = intent.getParcelableArrayListExtra<Product>("orderedProducts") ?: emptyList()

        // ✅ Retrieve payment details
        val paymentMethod = intent.getStringExtra("paymentMethod") ?: "Unknown"
        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)

        tvConfirmationMessage.text = "Your order has been placed successfully!"
        tvPaymentDetails.text = "Payment Method: $paymentMethod\nTotal Paid: ₱$totalAmount"

        // ✅ Pass ordered products when creating Order instance
        val order = Order("Order #${System.currentTimeMillis()}", "Placed", orderedProducts)
        saveOrder(order)

        btnBackToHome.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun saveOrder(order: Order) {
        val sharedPreferences = getSharedPreferences("OrdersPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Retrieve existing orders
        val gson = Gson()
        val orderList: MutableList<Order> = getSavedOrders().toMutableList()

        // Add new order
        orderList.add(order)

        // Save updated list
        val json = gson.toJson(orderList)
        editor.putString("orders", json)
        editor.apply() // ✅ Apply changes to save them
    }

    // Function to get saved orders
    private fun getSavedOrders(): List<Order> {
        val sharedPreferences = getSharedPreferences("OrdersPref", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("orders", null)
        val type = object : TypeToken<List<Order>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
