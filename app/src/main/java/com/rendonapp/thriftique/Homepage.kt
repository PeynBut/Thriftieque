package com.rendonapp.thriftique


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clohing.ItemDetailActivity
import clothing.CartActivity
import clothing.CartAdapter
import com.google.android.material.navigation.NavigationView

class Homepage : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var clothingAdapter: CartAdapter
    private lateinit var itemList: List<CartItem> // Change to CartItem
    private lateinit var tv_seeAll: TextView
    private lateinit var cart: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        setupNavigationDrawer()
        setupRecyclerView()

        tv_seeAll = findViewById(R.id.tv_seeAll)
        cart = findViewById(R.id.cart)

        tv_seeAll.setOnClickListener {
            vibrate() // Trigger vibration on text click
            val intent = Intent(this, SeeAllItemsActivity::class.java)
            startActivity(intent)
        }

        // Handle the Cart icon click
        cart.setOnClickListener {
            vibrate() // Trigger vibration on icon click
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    // 📌 Setup the Navigation Drawer
    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        findViewById<View>(R.id.menu).setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, Homepage::class.java))
                R.id.nav_profile -> startActivity(Intent(this, Homepage::class.java))
                R.id.nav_settings -> startActivity(Intent(this, Homepage::class.java))
                R.id.nav_logout -> finish()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    // 📌 Setup RecyclerView for CartItems
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Dynamic grid layout

        // Sample data for CartItems (this data should match CartItem properties)
        itemList = listOf(
            CartItem("1", 101), // Assuming CartItem has userId and productId
            CartItem("1", 102),
            CartItem("1", 103),
            CartItem("1", 104)
        )

        // Set up adapter with item click listener
        clothingAdapter = CartAdapter(this, itemList, 1, this::onItemClicked, this::onRemoveItemClicked)

        recyclerView.adapter = clothingAdapter
    }

    // 📌 Function triggered when an item is clicked
    private fun onItemClicked(item: CartItem) {
        vibrate() // Vibrate on click
        val intent = Intent(this, ItemDetailActivity::class.java)
        intent.putExtra("ITEM_DATA", item) // Pass the CartItem instead of ClothingItem

        startActivity(intent)
    }

    // 📌 Function triggered when remove button is clicked
    private fun onRemoveItemClicked(item: CartItem) {
        // Handle item removal logic here (maybe update cart, etc.)
        // You can update the adapter and remove the item from the list
        val updatedList = itemList.toMutableList()
        updatedList.remove(item)
        clothingAdapter = CartAdapter(this, updatedList, 1, this::onItemClicked, this::onRemoveItemClicked)
        recyclerView.adapter = clothingAdapter
        vibrate() // Trigger vibration on remove action
    }

    // 📌 Vibration Feedback
    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
