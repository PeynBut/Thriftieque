package Products

import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Constants
import com.bumptech.glide.Glide
import com.example.android.models.Product
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rendonapp.thriftique.R
import clothing.CartActivity
import com.example.android.models.ApiResponse
import com.rendonapp.thriftique.CartItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var suggestedProductsAdapter: SuggestedProductsAdapter
    private lateinit var suggestedProductsList: MutableList<Product>
    private lateinit var rvSuggestedProducts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val ivProductImage: ImageView = findViewById(R.id.ivProductImage)
        val tvProductName: TextView = findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = findViewById(R.id.tvProductPrice)
        val btnAddToCart: Button = findViewById(R.id.btnAddToCart)
        val btnBuyNow: Button = findViewById(R.id.btnBuyNow)

        rvSuggestedProducts = findViewById(R.id.rvSuggestedProducts)
        rvSuggestedProducts.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Initialize list and adapter
        suggestedProductsList = mutableListOf()
        suggestedProductsAdapter =
            SuggestedProductsAdapter(this, suggestedProductsList) { product ->
                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            }
        rvSuggestedProducts.adapter = suggestedProductsAdapter

        // Get product data from intent
        val product = intent.getParcelableExtra<Product>("product")

        product?.let {
            tvProductName.text = it.name
            tvProductPrice.text = "₱${it.price}"

            val imageUrl = formatImageUrl(it.image)
            Glide.with(this)
                .load(imageUrl ?: R.drawable.user) // Placeholder image if null
                .into(ivProductImage)

            // Handle Add to Cart click
            btnAddToCart.setOnClickListener {
                product?.let { showAddToCartPopup(it) }
            }

            btnBuyNow.setOnClickListener {
                product?.let { proceedToCheckout(it) }
            }

            // Load suggested products immediately
            loadSuggestedProducts()
        }
    }

    private fun loadSuggestedProducts() {
        val apiService = RetrofitClient.instance

        apiService.getProducts().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.products != null) {
                    suggestedProductsList.clear()
                    suggestedProductsList.addAll(response.body()?.products!!)

                    // Print image URLs for debugging
                    for (product in suggestedProductsList) {
                        println("Product Image URL: ${product.image}")
                    }

                    suggestedProductsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        "Failed to load products",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun proceedToCheckout(product: Product) {
        val imageUrl = formatImageUrl(product.image)

        val intent = Intent(this, CheckoutActivity::class.java).apply {
            putExtra("selectedProduct", product)
            putExtra("productImage", imageUrl) // Pass the image URL
        }
        startActivity(intent)
    }

    private fun showAddToCartPopup(product: Product) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_to_cart, null)

        val ivProductImage: ImageView = view.findViewById(R.id.ivProductImage)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val etQuantity: EditText = view.findViewById(R.id.etQuantity)
        val btnConfirm: Button = view.findViewById(R.id.btnConfirmAddToCart)

        val imageUrl = formatImageUrl(product.image)
        Glide.with(this)
            .load(imageUrl ?: R.drawable.user) // Placeholder image if null
            .into(ivProductImage)

        tvProductPrice.text = "₱${product.price}"

        btnConfirm.setOnClickListener {
            val quantity = etQuantity.text.toString().trim().toIntOrNull() ?: 1

            val cartItem = CartItem(
                userId = 1, // Change this dynamically for actual user
                productId = product.id,
                quantity = quantity,
                productName = product.name,
                productImage = imageUrl,
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

    private fun formatImageUrl(imagePath: String?): String {
        val baseUrl = Constants.getBaseUrl(this)
        return when {
            imagePath.isNullOrEmpty() -> "" // Default empty string if no image
            imagePath.startsWith("http") -> imagePath.trim()
            imagePath.startsWith("uploads/") -> baseUrl + imagePath.removePrefix("uploads/")
            else -> baseUrl + imagePath.trim()
        }
    }
}
