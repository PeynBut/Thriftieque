package com.rendonapp.thriftique

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clothing.ClothingAdapter
import clothing.ClothingItem
import com.google.android.material.navigation.NavigationView
import java.util.Arrays

class homepage : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null
    private var toggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Setup Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout!!.addDrawerListener(toggle!!)
        toggle!!.syncState()

        findViewById<View>(R.id.menu).setOnClickListener {
            drawerLayout!!.openDrawer(
                navigationView
            )
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {}
                R.id.nav_profile -> {}
                R.id.nav_settings -> {}
                R.id.nav_logout -> {}
            }
            drawerLayout!!.closeDrawers()
            true
        }

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns

        // Sample data for RecyclerView
        val itemList = Arrays.asList(
            ClothingItem(R.drawable.user, "T-Shirt", "Comfortable cotton t-shirt"),
            ClothingItem(R.drawable.shopping_cart, "Jeans", "Stylish blue jeans"),
            ClothingItem(R.drawable.house, "Jacket", "Warm winter jacket") // Add more items here
        )

        recyclerView.adapter = ClothingAdapter(itemList)
    }
}