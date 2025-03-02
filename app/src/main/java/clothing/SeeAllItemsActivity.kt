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
                Log.d("FetchProducts", "Raw response: ${response.raw()}")
                Log.d("FetchProducts", "Response Code: ${response.code()}")

                val apiResponse = response.body()

                if (response.isSuccessful && apiResponse != null) {
                    Log.d("FetchProducts", "Response Body: $apiResponse")

                    val products = apiResponse.data

                    if (products.isNullOrEmpty()) {
                        Log.e("FetchProducts", "API returned empty or null product list")
                        showToast("No products available")
                    } else {
                        itemList.clear()
                        itemList.addAll(products.map { product ->
                            CartItem(
                                userId = 0,
                                productId = product.id,
                                quantity = 1,
                                productName = product.name ?: "Unknown Product",
                                productImage = product.image ?: "",
                                productPrice = product.price
                            )
                        })
                        cartAdapter.notifyDataSetChanged()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("FetchProducts", "API Error: ${response.code()} - $errorBody")
                    showToast("Failed to load products: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("FetchProducts", "Network error: ${t.message}")
                showToast("Failed to fetch products. Check your internet connection.")
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
