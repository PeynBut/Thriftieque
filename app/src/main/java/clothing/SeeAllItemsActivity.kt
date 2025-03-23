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
import com.example.android.models.ApiResponse
import com.example.android.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max

class SeeAllItemsActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val itemList: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_items)

        setupRecyclerView()
        fetchProducts()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewAll)
        recyclerView.layoutManager = GridLayoutManager(this, calculateNoOfColumns())
        productAdapter = ProductAdapter(this, itemList)
        recyclerView.adapter = productAdapter
    }

    private fun fetchProducts() {
        RetrofitClient.instance.getProducts().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (!response.isSuccessful) {
                    logError("API Error", "Response Code: ${response.code()}")
                    showToast("Failed to load products: ${response.code()}")
                    return
                }

                val apiResponse = response.body()
                if (apiResponse?.products.isNullOrEmpty()) {
                    logError("FetchProducts", "No products available")
                    showToast("No products available")
                    return
                }

                itemList.clear()
                itemList.addAll(apiResponse?.products ?: emptyList())
                productAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                logError("Network error", t.message ?: "Unknown error")
                showToast("Failed to fetch products. Check your internet connection.")
            }
        })
    }

    private fun calculateNoOfColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val columnWidth = 300
        return max((dpWidth / columnWidth).toInt(), 2)
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logError(tag: String, message: String) {
        Log.e(tag, message)
    }
}
