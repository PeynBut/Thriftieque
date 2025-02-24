package Products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.android.models.Product
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rendonapp.thriftique.R
import clothing.CartActivity
import com.rendonapp.thriftique.CartItem
import message.MessageActivity

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val ivProductImage: ImageView = findViewById(R.id.ivProductImage)
        val tvProductName: TextView = findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = findViewById(R.id.tvProductPrice)
        val btnAddToCart: Button = findViewById(R.id.btnAddToCart)
        val btnBuyNow: Button = findViewById(R.id.btnBuyNow)

        // Get product data from intent
        val product = intent.getParcelableExtra<Product>("product")

        product?.let {
            tvProductName.text = it.name
            tvProductPrice.text = "$${it.price}"

            Glide.with(this)
                .load(it.image ?: R.drawable.user)
                .into(ivProductImage)

            // Handle Add to Cart click
            btnAddToCart.setOnClickListener {
                showAddToCartPopup(product)
            }

            // Handle Buy Now click
            btnBuyNow.setOnClickListener {
                val checkoutIntent = Intent(this, MessageActivity::class.java)
                checkoutIntent.putExtra("product", product)
                startActivity(checkoutIntent)
            }
        }
    }

    private fun showAddToCartPopup(product: Product) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_to_cart, null)

        val ivProductImage: ImageView = view.findViewById(R.id.ivProductImage)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val etQuantity: EditText = view.findViewById(R.id.etQuantity)
        val etNotes: EditText = view.findViewById(R.id.etNotes)
        val btnConfirm: Button = view.findViewById(R.id.btnConfirmAddToCart)

        // Load product image dynamically using Glide
        Glide.with(this)
            .load(product.image ?: R.drawable.blouse) // Placeholder if null
            .into(ivProductImage)

        tvProductPrice.text = "$${product.price}"

        btnConfirm.setOnClickListener {
            val quantity = etQuantity.text.toString().trim()
            if (quantity.isEmpty()) {
                etQuantity.error = "Enter quantity"
                return@setOnClickListener
            }

            val quantityInt = quantity.toIntOrNull() ?: 1

            // Create a new CartItem including the image URL
            val cartItem = CartItem(
                userId = 1,
                productId = product.id,
                quantity = quantityInt,
                productName = product.name,
                productImage = product.image ?: "", // Ensure image is passed
                productPrice = product.price
            )

            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("cartItem", cartItem)
            startActivity(intent)

            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}
