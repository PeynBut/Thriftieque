package com.rendonapp.thriftique

import ClothingItem
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemDetailActivity : Activity() {
    private lateinit var itemImage: ImageView
    private lateinit var itemTitle: TextView
    private lateinit var itemDescription: TextView
    private lateinit var itemPrice: TextView
    private lateinit var addToCartButton: Button
    private val userId = 1 // Simulated user ID (change based on real login system)
    private var clothingItem: ClothingItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        // Initialize views
        itemImage = findViewById(R.id.product_image)
        itemTitle = findViewById(R.id.product_title)
        itemDescription = findViewById(R.id.product_description)
        itemPrice = findViewById(R.id.product_price)
        addToCartButton = findViewById(R.id.btn_add_to_cart)

        // Retrieve item data from intent using Parcelable
        clothingItem = intent.getParcelableExtra("ITEM_DATA")

        clothingItem?.let { item ->
            // Set image safely
            item.imageResId?.let { resId ->
                itemImage.setImageResource(resId)
            } ?: itemImage.setImageResource(R.drawable.user) // Use a placeholder image if null

            // Set item details
            itemTitle.text = item.title
            itemDescription.text = "Product Description:\n${item.description}"
            itemPrice.text = "$${item.price}"
        } ?: run {
            Toast.makeText(this, "Error: Item data not found", Toast.LENGTH_SHORT).show()
        }

        // Set up add to cart button
        addToCartButton.setOnClickListener {
            addToCart()
        }
    }

    private fun addToCart() {
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        clothingItem?.let { item ->
            val cartItem = CartItem(
                userId,
                item.id,
                1, // Default quantity
                item.title,
                item.imageUrl ?: "", // Provide an empty string if null
                item.price
            )

            RetrofitClient.instance.addToCart(cartItem).enqueue(object : Callback<ApiResponse?> {
                override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@ItemDetailActivity, "Added to Cart!", Toast.LENGTH_SHORT).show()
                        vibrate()
                    } else {
                        Toast.makeText(this@ItemDetailActivity, "Failed to add item", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                    Log.e("ItemDetail", "Error adding to cart: ${t.message}")
                    Toast.makeText(this@ItemDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } ?: run {
            Toast.makeText(this, "Invalid item", Toast.LENGTH_SHORT).show()
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
