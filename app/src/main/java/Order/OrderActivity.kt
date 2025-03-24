package Order

import OrderAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rendonapp.thriftique.databinding.BottomNavOrderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import api.Order
import api.OrderResponse

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: BottomNavOrderBinding
    private lateinit var orderAdapter: OrderAdapter
    private val TAG = "OrderActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomNavOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the back button
        binding.orderToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Initialize RecyclerView
        orderAdapter = OrderAdapter(emptyList())
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrders.adapter = orderAdapter

        val userId = getUserIdFromPreferences()
        if (userId != -1) {
            fetchOrders(userId)
        }

        // Handle category button clicks
        binding.buttonPlaceOrder.setOnClickListener { filterOrders("Place Order") }
        binding.buttonPreparing.setOnClickListener { filterOrders("Preparing") }
        binding.buttonReady.setOnClickListener { filterOrders("Ready") }
        binding.buttonCompleted.setOnClickListener { filterOrders("Completed") }
    }

    private fun filterOrders(status: String) {
        val filteredOrders = getSavedOrders().filter { it.status == status }
        orderAdapter.updateOrders(filteredOrders)
    }
    private fun fetchOrders(userId: Int) {
        RetrofitClient.orderInstance.getOrders(userId).enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { orderResponse ->
                        if (!orderResponse.error) { // ✅ Check for errors
                            saveOrders(orderResponse.orders)  // ✅ Now we have orders!
                            orderAdapter.updateOrders(orderResponse.orders)
                        } else {
                            Log.e(TAG, "Error: ${orderResponse.message}")
                            showToast(orderResponse.message)
                        }
                    } ?: run {
                        Log.e(TAG, "Response body is null")
                        showToast("Failed to fetch orders")
                    }
                } else {
                    Log.e(TAG, "Error fetching orders: ${response.errorBody()?.string()}")
                    showToast("Failed to fetch orders")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Log.e(TAG, "Network error: ${t.message}")
                showToast("Network error: ${t.message}")
            }
        })
    }


        private fun saveOrders(orders: List<Order>) {
        val sharedPreferences = getSharedPreferences("OrdersPref", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("orders", Gson().toJson(orders)).apply()
    }

    private fun getSavedOrders(): List<Order> {
        val sharedPreferences = getSharedPreferences("OrdersPref", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("orders", null) ?: return emptyList()
        return Gson().fromJson(json, object : TypeToken<List<Order>>() {}.type)
    }

    private fun getUserIdFromPreferences(): Int {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE) // ✅ Corrected
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId == -1) {
            Log.e(TAG, "❌ User ID not found in SharedPreferences!")
            showToast("User not logged in")
        } else {
            Log.d(TAG, "✅ Retrieved User ID: $userId")
        }

        return userId
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}