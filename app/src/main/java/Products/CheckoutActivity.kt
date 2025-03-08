package Products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.android.models.Product
import com.rendonapp.thriftique.R

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val ivCheckoutProductImage: ImageView = findViewById(R.id.ivCheckout_ProductImage)
        val tvCheckoutProductName: TextView = findViewById(R.id.tvCheckout_ProductName)
        val tvCheckoutPrice: TextView = findViewById(R.id.tvCheckout_Price)
        val tvCheckoutQuantity: TextView = findViewById(R.id.tvCheckout_Quantity)
        val tvCheckoutTotal: TextView = findViewById(R.id.tvCheckout_Total)
        val btnProceedToPayment: Button = findViewById(R.id.btnProceedTo_Payment)

        // Retrieve selected product and quantity
        val product = intent.getParcelableExtra<Product>("selectedProduct")
        val quantity = intent.getIntExtra("selectedQuantity", 1) // Default quantity is 1

        product?.let {
            tvCheckoutProductName.text = it.name
            tvCheckoutPrice.text = "$${it.price}"
            tvCheckoutQuantity.text = "Quantity: $quantity"

            val totalPrice = it.price * quantity
            tvCheckoutTotal.text = "Total: $$totalPrice"

            // Load the exact image the user clicked on
            Glide.with(this)
                .load(it.image?.takeIf { it.isNotEmpty() } ?: R.drawable.user)
                .placeholder(R.drawable.user) // Show while loading
                .error(R.drawable.user) // Use an existing image if loading fails
                .into(ivCheckoutProductImage)
        }

        // Handle Proceed to Payment click
        btnProceedToPayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("selectedProduct", product) // Pass the product data
            intent.putExtra("selectedQuantity", quantity) // Pass quantity
            startActivity(intent)
        }
    }
}
