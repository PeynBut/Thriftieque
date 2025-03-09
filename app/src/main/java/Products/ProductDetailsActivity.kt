package Products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import api.Constants
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
            tvProductPrice.text = "₱${it.price}"

            // Handle image URL
            val baseUrl = Constants.getBaseUrl(this)
            val imageUrl = when {
                it.image.isNullOrEmpty() -> null  // Use default placeholder
                it.image.startsWith("http") -> it.image.trim()  // Already a full URL
                it.image.startsWith("uploads/") -> baseUrl + it.image.removePrefix("uploads/") // Prevent double "uploads/"
                else -> baseUrl + it.image.trim() // Standard case
            }

            // Load the product image into the ImageView
            Glide.with(this)
                .load(imageUrl ?: R.drawable.user) // Placeholder image if null
                .into(ivProductImage)

            // Handle Add to Cart click
            btnAddToCart.setOnClickListener {
                showAddToCartPopup(product)
            }

            btnBuyNow.setOnClickListener {
                product?.let { selectedProduct ->
                    proceedToCheckout(selectedProduct)
                }
            }


        }
    }
    private fun proceedToCheckout(product: Product) {
        val intent = Intent(this, CheckoutActivity::class.java)
        intent.putExtra("selectedProduct", product)
        startActivity(intent)
    }


    private fun showAddToCartPopup(product: Product) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_to_cart, null)

        val ivProductImage: ImageView = view.findViewById(R.id.ivProductImage)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val etQuantity: EditText = view.findViewById(R.id.etQuantity)
        val etNotes: EditText = view.findViewById(R.id.etNotes)
        val btnConfirm: Button = view.findViewById(R.id.btnConfirmAddToCart)

        // Handle image URL
        val baseUrl = Constants.getBaseUrl(this)
        val imageUrl = when {
            product.image.isNullOrEmpty() -> null  // Use default placeholder
            product.image.startsWith("http") -> product.image.trim()  // Already a full URL
            product.image.startsWith("uploads/") -> baseUrl + product.image.removePrefix("uploads/") // Prevent double "uploads/"
            else -> baseUrl + product.image.trim() // Standard case
        }

        // Load product image dynamically using Glide
        Glide.with(this)
            .load(imageUrl ?: R.drawable.user) // Placeholder image if null
            .into(ivProductImage)

        // Set product price in the dialog
        tvProductPrice.text = "₱${product.price}"

        btnConfirm.setOnClickListener {
            val quantity = etQuantity.text.toString().trim()
            if (quantity.isEmpty()) {
                etQuantity.error = "Enter quantity"
                return@setOnClickListener
            }

            val quantityInt = quantity.toIntOrNull() ?: 1

            val baseUrl = Constants.getBaseUrl(this)
            val imageUrl = when {
                product.image.isNullOrEmpty() -> "" // Default empty string if no image
                product.image.startsWith("http") -> product.image.trim()
                product.image.startsWith("uploads/") -> baseUrl + product.image.removePrefix("uploads/")
                else -> baseUrl + product.image.trim()
            }

            val cartItem = CartItem(
                userId = 1, // Change this dynamically for actual user
                productId = product.id,
                quantity = quantityInt,
                productName = product.name,
                productImage = imageUrl, // Ensure image URL is set correctly
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
