package com.rendonapp.thriftique

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clothing.CartAdapter
import com.example.android.models.ApiResponse
import com.example.android.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max

class SeeAllItemsActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private var itemList: MutableList<CartItem> = mutableListOf() // List for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_items)

        recyclerView = findViewById(R.id.recyclerViewAll)
        recyclerView.layoutManager = GridLayoutManager(this, calculateNoOfColumns())

        // Initialize adapter
        cartAdapter = CartAdapter(this, itemList, ::onItemClicked, ::onRemoveClicked)
        recyclerView.adapter = cartAdapter

        // Fetch products from API
        fetchProducts()
    }

    // ✅ Fetch products from backend API
    private fun fetchProducts() {
        RetrofitClient.instance.getProducts().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val products = apiResponse?.data ?: emptyList() // Safe call for null

                    if (products.isNotEmpty()) {
                        itemList.clear()
                        itemList.addAll(products.map { product ->
                            CartItem(
                                userId = 0,
                                productId = product.id,
                                quantity = 1,
                                productName = product.name ?: "Unknown Product", // Default if name is null
                                productImage = product.image ?: "", // Default if image is null
                                productPrice = product.price // Assuming price is non-nullable in Product model
                            )
                        })
                        cartAdapter.notifyDataSetChanged()
                    } else {
                        showToast("No products available")
                    }
                } else {
                    showToast("Failed to load products: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("FetchProducts", "Error fetching products: ${t.message}")
                showToast("Failed to fetch products. Check your connection.")
            }
        })
    }




    // ✅ Dynamically calculate number of columns
    private fun calculateNoOfColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val columnWidth = 180 // Column width estimate
        return max((dpWidth / columnWidth).toInt(), 2) // Minimum 2 columns
    }

    // ✅ Handle item click: Navigate to ItemDetailActivity
    private fun onItemClicked(item: CartItem) {
        vibrate() // Vibration feedback
        val intent = Intent(this, ItemDetailActivity::class.java).apply {
            putExtra("ITEM_DATA", item) // Pass item data
        }
        startActivity(intent)
    }

    // ✅ Handle remove button click
    private fun onRemoveClicked(item: CartItem) {
        val position = itemList.indexOf(item)
        if (position != -1) {
            itemList.removeAt(position)
            cartAdapter.notifyItemRemoved(position) // Optimized notify
            showToast("Removed ${item.productName} from the list")
        }
    }

    // ✅ Vibration feedback
    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    // ✅ Helper function for toasts
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
