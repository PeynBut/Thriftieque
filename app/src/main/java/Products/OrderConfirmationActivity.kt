package Products

import OrderedProductAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clothing.CartStorage
import com.example.android.models.Product
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

        // ✅ Setup RecyclerView

        adapter = OrderedProductAdapter(orderedProducts)

        CartStorage.saveCart(this, emptyList())

        btnBackToHome.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
