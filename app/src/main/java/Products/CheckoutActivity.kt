package Products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rendonapp.thriftique.R
import com.rendonapp.thriftique.CartItem

class CheckoutActivity : AppCompatActivity() {

    private lateinit var rvCheckoutItems: RecyclerView
    private lateinit var tvTotalAmount: TextView
    private lateinit var btnProceedToPayment: Button
    private lateinit var checkoutAdapter: CheckoutAdapter
    private lateinit var selectedItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Initialize UI elements
        rvCheckoutItems = findViewById(R.id.rvCheckout_Items)
        tvTotalAmount = findViewById(R.id.tvCheckout_Total)
        btnProceedToPayment = findViewById(R.id.btnProceedTo_Payment)

        // Retrieve selected items from intent
        selectedItems = intent.getParcelableArrayListExtra("selected_items") ?: arrayListOf()

        // Calculate and display total price with proper currency formatting
        val totalPrice = selectedItems.sumOf { it.productPrice * it.quantity }
        tvTotalAmount.text = "Total: ₱%.2f".format(totalPrice)

        // Setup RecyclerView
        checkoutAdapter = CheckoutAdapter(this, selectedItems)
        rvCheckoutItems.layoutManager = LinearLayoutManager(this)
        rvCheckoutItems.adapter = checkoutAdapter

        // Handle Proceed to Payment click
        btnProceedToPayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java).apply {
                putParcelableArrayListExtra("selected_items", selectedItems)
                putExtra("total_price", totalPrice)
            }
            startActivity(intent)
        }
    }
}
