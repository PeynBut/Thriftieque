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
import com.rendonapp.thriftique.CartItem // Import your CartItem class

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartList = mutableListOf<CartItem>()
    private lateinit var backBtn: ImageView
    private lateinit var menu: ImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    // In CartActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        backBtn = findViewById(R.id.back_btn)
        menu = findViewById(R.id.menu)
        recyclerView = findViewById(R.id.cartRecyclerView)

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(this, cartList, {}, ::removeItem)
        recyclerView.adapter = cartAdapter

        // Check for new cart item
        val newCartItem = intent.getParcelableExtra<CartItem>("cartItem")
        newCartItem?.let {
            cartList.add(it)
            cartAdapter.notifyDataSetChanged()
        }
        // Setup Navigation Drawer
        setupNavigationDrawer()

        // Back button listener with vibration
        backBtn.setOnClickListener {
            vibrate()
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }


    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        if (drawerLayout == null || navigationView == null) {
            Log.e("CartActivity", "DrawerLayout or NavigationView not found!")
            return
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, Homepage::class.java))
                // Add your other navigation actions here
            }
            drawerLayout.closeDrawers()
            true
        }

        menu.setOnClickListener {
            vibrate()
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun removeItem(cartItem: CartItem) {
        cartList.remove(cartItem)
        cartAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Removed ${cartItem.productId} from cart", Toast.LENGTH_SHORT).show()
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
