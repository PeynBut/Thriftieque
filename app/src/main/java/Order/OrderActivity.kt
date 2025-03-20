package Order

import OrderAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rendonapp.thriftique.databinding.BottomNavOrderBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: BottomNavOrderBinding
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Correct ViewBinding usage
        binding = BottomNavOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Setup RecyclerView with only "Place Order" items initially
        val initialOrders = getDummyOrders().filter { it.status == "Place Order" }
        orderAdapter = OrderAdapter(initialOrders)
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrders.adapter = orderAdapter

        // ✅ Handle category button clicks
        binding.buttonPlaceOrder.setOnClickListener { filterOrders("Place Order") }
        binding.buttonPreparing.setOnClickListener { filterOrders("Preparing") }
        binding.buttonReady.setOnClickListener { filterOrders("Ready") }
        binding.buttonCompleted.setOnClickListener { filterOrders("Completed") }
    }

    private fun filterOrders(status: String) {
        val filteredOrders = getDummyOrders().filter { it.status == status }
        orderAdapter.updateOrders(filteredOrders)
    }

    private fun getDummyOrders(): List<Order> {
        return listOf(
            Order("Order #1", "Place Order"),
            Order("Order #2", "Preparing"),
            Order("Order #3", "Ready"),
            Order("Order #4", "Completed"),
        )
    }
}
