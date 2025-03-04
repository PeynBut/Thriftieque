package clothing

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.rendonapp.thriftique.Homepage
import com.rendonapp.thriftique.R
import com.rendonapp.thriftique.CartItem // Import your CartItem class

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartList = mutableListOf<CartItem>()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize UI elements
        toolbar = findViewById(R.id.topAppBar)
        drawerLayout = findViewById(R.id.drawer_layout)
        recyclerView = findViewById(R.id.cartRecyclerView)

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(this, cartList, {}, ::removeItem)
        recyclerView.adapter = cartAdapter

        // Handle cart item addition (Prevents duplicate entries)
        val newCartItem = intent.getParcelableExtra<CartItem>("cartItem")
        newCartItem?.let { item ->
            if (!cartList.contains(item)) {
                cartList.add(item)
                cartAdapter.notifyItemInserted(cartList.size - 1)
            }
        }

        // Setup Navigation Drawer
        setupNavigationDrawer()

        // Back button listener
        toolbar.setNavigationOnClickListener {
            vibrate()
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun setupNavigationDrawer() {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, Homepage::class.java))
                // Add other navigation actions here
            }
            drawerLayout.closeDrawers()
            true
        }

        // Handle menu icon click (drawer open)
        toolbar.setNavigationOnClickListener {
            vibrate()
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun removeItem(cartItem: CartItem) {
        val index = cartList.indexOf(cartItem)
        if (index != -1) {
            cartList.removeAt(index)
            cartAdapter.notifyItemRemoved(index)
            Toast.makeText(this, "Removed item from cart", Toast.LENGTH_SHORT).show()
        }

        // Check if cart is empty after removing an item
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
