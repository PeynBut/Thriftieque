package com.rendonapp.thriftique

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clothing.CartActivity
import clothing.CartAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class Homepage : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var clothingAdapter: CartAdapter
    private lateinit var homeClothingItems: List<CartItem>
    private lateinit var seeAllClothingItems: List<CartItem>
    private var cartItems: MutableList<CartItem> = mutableListOf()
    private lateinit var tvSeeAll: TextView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Sample data for Homepage
        homeClothingItems = listOf(
            CartItem(userId = 1, productId = 201, quantity = 1),
            CartItem(userId = 1, productId = 202, quantity = 1)
        )

        // Sample data for See All
        seeAllClothingItems = listOf(
            CartItem(userId = 1, productId = 301, quantity = 1),
            CartItem(userId = 1, productId = 302, quantity = 1)
        )

        // Initialize the RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        clothingAdapter = CartAdapter(this, homeClothingItems, this::onItemClicked, this::onAddToCartClicked)
        recyclerView.adapter = clothingAdapter

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        setupNavigationDrawer()

        tvSeeAll = findViewById(R.id.tv_seeAll)
        val cart = findViewById<ImageView>(R.id.cart)

        tvSeeAll.setOnClickListener {
            vibrate()
            startActivity(Intent(this, SeeAllItemsActivity::class.java))
        }
        cart.setOnClickListener {
            vibrate()
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    // 📌 Setup the Navigation Drawer
    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val toolbar = findViewById<MaterialToolbar>(R.id.menu) // Ensure toolbar is correctly set in XML

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
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

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_message, R.id.nav_home, R.id.nav_profile -> true
                else -> false
            }
        }
    }

    // 📌 Function triggered when an item is clicked
    private fun onItemClicked(item: CartItem) {
        vibrate()
        val intent = Intent(this, ItemDetailActivity::class.java)
        intent.putExtra("ITEM_DATA", item) // Ensure CartItem implements Parcelable
        startActivity(intent)
    }

    // 📌 Function triggered when add to cart button is clicked
    private fun onAddToCartClicked(item: CartItem) {
        if (!cartItems.contains(item)) {
            cartItems.add(item)
            // Optionally, show a message to the user that the item has been added
            // Example: Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
        }
    }

    // 📌 Vibration Feedback
    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
