package com.rendonapp.thriftique

import ClothingItem
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
import clothing.CartActivity
import clothing.ClothingAdapter
import clothing.ItemDetailActivity
import com.google.android.material.navigation.NavigationView

class Homepage : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var clothingAdapter: ClothingAdapter
    private lateinit var itemList: List<ClothingItem>
    private lateinit var tv_seeAll : TextView
    private lateinit var cart : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        setupNavigationDrawer()
        setupRecyclerView()

        tv_seeAll = findViewById(R.id.tv_seeAll)
        cart = findViewById(R.id.cart)

        tv_seeAll.setOnClickListener {
            val intent = Intent(this, SeeAllItemsActivity::class.java)
            startActivity(intent)
        }

        // Handle the Cart icon click
        cart.setOnClickListener {
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

    // 📌 Setup RecyclerView for Clothing Items
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Dynamic grid layout

        // Sample data for testing
        itemList = listOf(
            ClothingItem(1, "T-Shirt", R.drawable.cool_tshirt, "Comfortable cotton t-shirt", 19.99, 1),
            ClothingItem(2, "Jeans", R.drawable.skinny_jeans, "Stylish blue jeans", 39.99, 1),
            ClothingItem(3, "Jacket", R.drawable.long_sleeve, "Warm winter jacket", 59.99, 1),
            ClothingItem(4, "Dress", R.drawable.blouse, "Elegant evening dress", 29.99, 1)
        )

        // Set up adapter with item click listener
        clothingAdapter = ClothingAdapter(this, itemList, 1, this::onItemClicked)

        recyclerView.adapter = clothingAdapter
    }

    // 📌 Function triggered when an item is clicked
    private fun onItemClicked(item: ClothingItem) {
        vibrate() // Vibrate on click
        val intent = Intent(this, ItemDetailActivity::class.java)
        intent.putExtra("ITEM_DATA", item)

        startActivity(intent)
    }

    // 📌 Vibration Feedback
    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}

