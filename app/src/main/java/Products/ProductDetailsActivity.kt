package Products

import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Constants
import com.bumptech.glide.Glide
import com.example.android.models.Product
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

    private var quantity = 1 // Default quantity
    private lateinit var tvQuantity: TextView
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val ivProductImage: ImageView = findViewById(R.id.ivProductImage)
        val tvProductName: TextView = findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = findViewById(R.id.tvProductPrice)
        val btnAddToCart: Button = findViewById(R.id.btnAddToCart)
        val btnBuyNow: Button = findViewById(R.id.btnBuyNow)

        // Quantity buttons
        val btnIncreaseQuantity: ImageButton = findViewById(R.id.btnIncreaseQuantity)
        val btnDecreaseQuantity: ImageButton = findViewById(R.id.btnDecreaseQuantity)
        tvQuantity = findViewById(R.id.tvQuantity)

        rvSuggestedProducts = findViewById(R.id.rvSuggestedProducts)
        rvSuggestedProducts.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Initialize list and adapter
        suggestedProductsList = mutableListOf()
        suggestedProductsAdapter = SuggestedProductsAdapter(this, suggestedProductsList) { selectedProduct ->
            updateProductDetails(selectedProduct)
        }

        rvSuggestedProducts.adapter = suggestedProductsAdapter

        // Get product data from intent
        product = intent.getParcelableExtra("product")

        product?.let {
            tvProductName.text = it.name
            tvProductPrice.text = "₱${it.price}"

            val imageUrl = formatImageUrl(it.image)
            Glide.with(this).load(imageUrl).placeholder(R.drawable.user).into(ivProductImage)
        } ?: run {
            Toast.makeText(this, "Error loading product details", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Increase Quantity
        btnIncreaseQuantity.setOnClickListener {
            quantity++
            updateQuantityUI()
        }

        // Decrease Quantity (Minimum 1)
        btnDecreaseQuantity.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityUI()
            }
        }

        // Buy Now Button
        btnBuyNow.setOnClickListener {
            product?.let { proceedToCheckout(it) }
        }

        // Add to Cart (Automatically Adds to Cart)
        btnAddToCart.setOnClickListener {
            addToCart()
        }

        // Load suggested products
        loadSuggestedProducts()
    }

    private fun updateProductDetails(selectedProduct: Product) {
        product = selectedProduct

        // Update UI elements
        findViewById<TextView>(R.id.tvProductName).text = product!!.name
        findViewById<TextView>(R.id.tvProductPrice).text = "₱${product!!.price}"

        val imageUrl = formatImageUrl(product!!.image)
        Glide.with(this).load(imageUrl).placeholder(R.drawable.user).into(findViewById(R.id.ivProductImage))

        // Reset quantity to 1 when a new product is selected
        quantity = 1
        updateQuantityUI()
    }

    private fun proceedToCheckout(product: Product) {
        val cartItem = CartItem(
            userId = 1,  // Replace with actual user ID dynamically
            productId = product.id,
            quantity = quantity,
            productName = product.name,
            productImage = formatImageUrl(product.image),
            productPrice = product.price
        )

        val selectedItems = arrayListOf(cartItem) // Convert to an ArrayList<CartItem>

        val intent = Intent(this, CheckoutActivity::class.java).apply {
            putParcelableArrayListExtra("selected_items", selectedItems)
        }
        startActivity(intent)
    }

    // Update the quantity in the UI
    private fun updateQuantityUI() {
        tvQuantity.text = quantity.toString()
    }

    // Function to add to cart automatically
    private fun addToCart() {
        product?.let {
            val cartItem = CartItem(
                userId = 1,  // Replace with actual user ID dynamically
                productId = it.id,
                quantity = quantity,
                productName = it.name,
                productImage = formatImageUrl(it.image),
                productPrice = it.price
            )

            // Send the cart item to the cart activity
            val intent = Intent(this, CartActivity::class.java).apply {
                putExtra("cartItem", cartItem)
            }
            startActivity(intent)

            // Show a toast message when added to cart
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "Error adding to cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSuggestedProducts() {
        val apiService = RetrofitClient.instance

        apiService.getProducts().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.products != null) {
                    suggestedProductsList.clear()
                    suggestedProductsList.addAll(response.body()?.products!!)

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

    private fun formatImageUrl(imagePath: String?): String {
        val baseUrl = Constants.getBaseUrl(this)
        return when {
            imagePath.isNullOrEmpty() -> ""
            imagePath.startsWith("http") -> imagePath.trim()
            imagePath.startsWith("uploads/") -> baseUrl + imagePath.removePrefix("uploads/")
            else -> baseUrl + imagePath.trim()
        }
    }
}
