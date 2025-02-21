package clothing

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.rendonapp.thriftique.Homepage
import com.rendonapp.thriftique.R
import com.rendonapp.thriftique.CartItem // Import the correct CartItem

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartList = mutableListOf<CartItem>()
    private lateinit var backBtn: ImageView
    private lateinit var menu: ImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize views
        backBtn = findViewById(R.id.back_btn)
        menu = findViewById(R.id.menu)
        recyclerView = findViewById(R.id.cartRecyclerView)

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add sample data to cartList (replace with actual data if available)
        cartList.add(CartItem("101", 2)) // Sample CartItem (productId, quantity)
        cartList.add(CartItem("102", 1))
        cartList.add(CartItem("103", 3))

        // Setup the RecyclerView Adapter with proper CartItem type
        cartAdapter = CartAdapter(
            this,
            itemList = cartList,
            layoutType = 1, // 1 for list layout (change to 2 for grid layout)
            itemClickListener = { cartItem ->
                // Handle item click (navigate to item details, or leave empty if no action needed)
            },
            removeClickListener = { cartItem ->
                removeItem(cartItem) // Pass CartItem to the removeItem function
            }
        )

        recyclerView.adapter = cartAdapter

        // Setup Navigation Drawer
        setupNavigationDrawer()

        // Back button listener with vibration
        backBtn.setOnClickListener {
            vibrate() // Trigger vibration
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    // 📌 Setup the Navigation Drawer
    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        if (drawerLayout == null || navigationView == null) {
            Log.e("CartActivity", "DrawerLayout or NavigationView not found!")
            return
        }

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation items
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, Homepage::class.java))
                R.id.nav_profile -> startActivity(Intent(this, Homepage::class.java)) // Use actual ProfileActivity
                R.id.nav_settings -> startActivity(Intent(this, Homepage::class.java)) // Use actual SettingsActivity
                R.id.nav_logout -> finish()
            }
            drawerLayout.closeDrawers()
            true
        }

        menu.setOnClickListener {
            vibrate() // Trigger vibration
            drawerLayout.openDrawer(GravityCompat.START) // Open the drawer when menu clicked
        }
    }

    // Remove item logic
    private fun removeItem(cartItem: CartItem) {
        cartList.remove(cartItem) // Remove CartItem from the list
        cartAdapter.notifyDataSetChanged() // Refresh the RecyclerView
        Toast.makeText(this, "Removed ${cartItem.productId} from cart", Toast.LENGTH_SHORT).show()
    }

    // Vibration Feedback
    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
