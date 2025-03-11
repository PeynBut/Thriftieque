package Products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rendonapp.thriftique.R
import com.rendonapp.thriftique.CartItem

class CheckoutActivity : AppCompatActivity() {

    private lateinit var rvCheckoutItems: RecyclerView
    private lateinit var tvTotalAmount: TextView
    private lateinit var btnProceedToPayment: Button
    private lateinit var checkoutAdapter: CheckoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        rvCheckoutItems = findViewById(R.id.rvCheckout_Items)
        tvTotalAmount = findViewById(R.id.tvCheckout_Total)
        btnProceedToPayment = findViewById(R.id.btnProceedTo_Payment)

        // Retrieve selected products
        val selectedProducts = intent.getParcelableArrayListExtra<CartItem>("selectedItems") ?: arrayListOf()

        // Calculate total price
        val totalAmount = selectedProducts.sumOf { it.productPrice * it.quantity }
        tvTotalAmount.text = "Total: ₱$totalAmount"

        // Setup RecyclerView
        checkoutAdapter = CheckoutAdapter(this, selectedProducts)
        rvCheckoutItems.layoutManager = LinearLayoutManager(this)
        rvCheckoutItems.adapter = checkoutAdapter

        // Handle Proceed to Payment click
        btnProceedToPayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putParcelableArrayListExtra("selectedItems", selectedProducts) // Pass all items
            startActivity(intent)
        }
    }
}
