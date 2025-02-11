package clothing

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.models.CartItem
import com.rendonapp.thriftique.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartList = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart) // Set the correct layout for the activity

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample cart items (replace this with actual data)
        cartList.add(CartItem("101", 2))
        cartList.add(CartItem("102", 1))
        cartList.add(CartItem("103", 3))

        // Initialize the adapter
        cartAdapter = CartAdapter(this, cartList) { cartItem ->
            removeItem(cartItem)  // The function to remove the cart item
        }


        // Set the adapter to the RecyclerView
        recyclerView.adapter = cartAdapter
    }

    // Remove item logic
    private fun removeItem(cartItem: CartItem) {
        cartList.remove(cartItem)
        cartAdapter.notifyDataSetChanged() // Refresh the RecyclerView
        Toast.makeText(this, "Removed ${cartItem.productId} from cart", Toast.LENGTH_SHORT).show()
    }
}
