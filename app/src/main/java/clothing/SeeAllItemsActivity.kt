package com.rendonapp.thriftique


import com.rendonapp.thriftique.CartItem // Import the correct CartItem
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clohing.ItemDetailActivity
import clothing.CartAdapter
import kotlin.math.max

class SeeAllItemsActivity : Activity() {
    private var recyclerView: RecyclerView? = null
    private var cartAdapter: CartAdapter? = null
    private var itemList: MutableList<CartItem> = mutableListOf() // List of CartItem for CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_items)

        recyclerView = findViewById(R.id.recyclerViewAll)

        // Set GridLayoutManager based on screen width
        recyclerView?.layoutManager = GridLayoutManager(this, calculateNoOfColumns())

        // Set up CartAdapter with click listeners
        cartAdapter = CartAdapter(this, itemList, 1, { item ->
            this.onItemClicked(item) // Item click listener
        }, { item ->
            this.onRemoveClicked(item) // Remove button click listener
        })

        recyclerView?.adapter = cartAdapter

        // Initialize the list with sample data
        itemList.addAll(sampleClothingItems)
        cartAdapter?.notifyDataSetChanged() // Notify the adapter of data changes
    }

    // Function to calculate the number of columns dynamically
    private fun calculateNoOfColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val dpWidth = widthPixels / displayMetrics.density
        val columnWidth = 180 // Approximate width of each column

        return max((dpWidth / columnWidth).toInt(), 2) // Ensure at least 2 columns
    }

    private val sampleClothingItems: List<CartItem>
        // Sample data for testing (using CartItem from com.example.android.models)
        get() {
            val items: MutableList<CartItem> = ArrayList()
            items.add(CartItem(userId = "1", productId = 1)) // Sample CartItem
            items.add(CartItem(userId = "1", productId = 2))
            items.add(CartItem(userId = "1", productId = 3))
            items.add(CartItem(userId = "1", productId = 4))
            return items
        }

    // Function triggered when an item is clicked
    private fun onItemClicked(item: CartItem) {
        vibrate() // Vibrate on item click
        val intent = Intent(this, ItemDetailActivity::class.java)
        intent.putExtra("ITEM_DATA", item) // Pass item data to the detail activity
        startActivity(intent)
    }

    // Function triggered when the remove button is clicked
    private fun onRemoveClicked(item: CartItem) {
        // Remove the item from the list
        itemList.remove(item)
        cartAdapter?.notifyDataSetChanged() // Notify the adapter to refresh the list
        Toast.makeText(this, "Removed ${item.productId} from the list", Toast.LENGTH_SHORT).show()
    }

    // Function for vibration feedback
    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
