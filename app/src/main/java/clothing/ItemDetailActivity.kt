package clothing

import ClothingItem
import RetrofitClient
import android.app.Activity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.android.models.ApiResponse
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemDetailActivity : Activity() {
    private var itemImage: ImageView? = null
    private var itemTitle: TextView? = null
    private var itemDescription: TextView? = null
    private var itemPrice: TextView? = null
    private var addToCartButton: Button? = null
    private val userId = 1 // Simulated user ID (change based on real login system)
    private var clothingItem: ClothingItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        // Initialize views
        itemImage = findViewById<ImageView>(R.id.product_image)
        itemTitle = findViewById<TextView>(R.id.product_title)
        itemDescription = findViewById<TextView>(R.id.product_description)
        itemPrice = findViewById<TextView>(R.id.product_price)
        addToCartButton = findViewById(R.id.btn_add_to_cart)

        // Retrieve item data from intent
        clothingItem = intent.getSerializableExtra("ITEM_DATA") as ClothingItem?

        if (clothingItem != null) {
            // Set item details safely
            clothingItem?.let { item ->
                itemImage?.setImageResource(item.imageResId)
                itemTitle?.text = item.title
                itemDescription?.text = "Product Description:\n${item.description}"
                itemPrice?.text = "$${item.price}" // Use price property instead of getPrice()
            }
        }

        // Set up the add to cart button
        addToCartButton?.setOnClickListener {
            addToCart()
        }
    }

    // Function to add the item to the cart
    private fun addToCart() {
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a cart item object
        val cartItem = CartItem(userId, clothingItem?.id ?: -1, 1)

        // API call to add item to the cart
        RetrofitClient.instance.addToCart(cartItem).enqueue(object : Callback<ApiResponse?> {
            override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@ItemDetailActivity, "Added to Cart!", Toast.LENGTH_SHORT).show()
                    vibrate() // Trigger vibration feedback when item is added
                } else {
                    Toast.makeText(this@ItemDetailActivity, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                Log.e("ItemDetail", "Error adding to cart: ${t.message}")
                Toast.makeText(this@ItemDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to trigger vibration feedback
    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
