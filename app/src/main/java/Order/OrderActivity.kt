    package Order


    import OrderAdapter
    import android.content.Context
    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.google.gson.Gson
    import com.google.gson.reflect.TypeToken
    import com.rendonapp.thriftique.databinding.BottomNavOrderBinding

    class OrderActivity : AppCompatActivity() {
        private lateinit var binding: BottomNavOrderBinding
        private lateinit var orderAdapter: OrderAdapter
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // ✅ First, initialize binding
            binding = BottomNavOrderBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // ✅ Then, set up the back button
            binding.orderToolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            val savedOrders = getSavedOrders()
            orderAdapter = OrderAdapter(savedOrders)

            binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewOrders.adapter = orderAdapter

            // ✅ Handle category button clicks
            binding.buttonPlaceOrder.setOnClickListener { filterOrders("Place Order") }
            binding.buttonPreparing.setOnClickListener { filterOrders("Preparing") }
            binding.buttonReady.setOnClickListener { filterOrders("Ready") }
            binding.buttonCompleted.setOnClickListener { filterOrders("Completed") }
        }


        private fun filterOrders(status: String) {
            val filteredOrders = getSavedOrders().filter { it.status == status }
            orderAdapter.updateOrders(filteredOrders)
        }

        private fun getSavedOrders(): List<Order> {
            val sharedPreferences = getSharedPreferences("OrdersPref", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("orders", null)
            val type = object : TypeToken<List<Order>>() {}.type
            val orders = json?.let { gson.fromJson<List<Order>>(it, type) } ?: emptyList()

            for (order in orders) {
                println("Order ID: ${order.orderId}, Status: ${order.status}, Products: ${order.products?.size ?: 0}")
            }

            return orders
        }

    }
