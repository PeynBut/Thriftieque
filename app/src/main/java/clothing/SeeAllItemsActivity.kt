package com.rendonapp.thriftique

import Products.ProductAdapter
import android.app.Activity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Constants
import com.example.android.models.ApiResponse
import com.example.android.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max

class SeeAllItemsActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var itemList: MutableList<Product> = mutableListOf() // ✅ Store Products instead of Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_items)

        recyclerView = findViewById(R.id.recyclerViewAll)
        recyclerView.layoutManager = GridLayoutManager(this, calculateNoOfColumns())

        // Initialize adapter
        productAdapter = ProductAdapter(this, itemList)
        recyclerView.adapter = productAdapter

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

                    val products = apiResponse.products
                    if (products.isNullOrEmpty()) {
                        Log.e("FetchProducts", "API returned empty or null product list")
                        showToast("No products available")
                    } else {
                        itemList.clear()
                        itemList.addAll(products) // ✅ Directly add products to list
                        productAdapter.notifyDataSetChanged()
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
        val columnWidth = 300 // Increase column width for bigger images
        return max((dpWidth / columnWidth).toInt(), 2) // Keep minimum 2 columns
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
