package com.rendonapp.thriftique

import Products.ProductAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clothing.CartActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.android.models.Product
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import message.MessageActivity


class Homepage : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var tvSeeAll: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        recyclerView = findViewById(R.id.Products)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter(this, productList)
        recyclerView.adapter = productAdapter

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        setupNavigationDrawer()
        fetchProducts()

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

    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val toolbar = findViewById<MaterialToolbar>(R.id.menu)

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, Homepage::class.java))
                    true
                }
                R.id.nav_logout -> {
                    finish()
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_message -> {
                    startActivity(Intent(this, MessageActivity::class.java))
                    false
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.selectedItemId = R.id.nav_home
    }

    private fun fetchProducts() {
        val url = "http://192.168.100.184/thriftique_db/includes/v1/Products/get_products.php"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                if (response.getBoolean("success")) {
                    val productsArray = response.getJSONArray("products")
                    val products = mutableListOf<Product>()

                    for (i in 0 until productsArray.length()) {
                        val item = productsArray.getJSONObject(i)
                        products.add(
                            Product(
                                id = item.getInt("id"),
                                name = item.getString("name"),
                                description = item.getString("description"),
                                price = item.getDouble("price"),
                                image = item.getString("image")
                            )
                        )
                    }
                    productAdapter.updateList(products)
                } else {
                    Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", "Error: ${error.message}")
            })

        Volley.newRequestQueue(this).add(request)
    }


    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
