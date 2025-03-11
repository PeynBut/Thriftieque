package clothing

import Products.CheckoutActivity
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.rendonapp.thriftique.Homepage
import com.rendonapp.thriftique.R
import com.rendonapp.thriftique.CartItem

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartList = mutableListOf<CartItem>()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: MaterialToolbar
    private lateinit var tvTotalPrice: TextView
    private lateinit var tvItemCount: TextView
    private lateinit var btnContinueShoppingBottom: MaterialButton
    private lateinit var btnPlaceOrder: MaterialButton
    private lateinit var emptyCartView: LinearLayout
    private lateinit var btnContinueShopping: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize UI elements
        toolbar = findViewById(R.id.topAppBar)
        drawerLayout = findViewById(R.id.drawer_layout)
        recyclerView = findViewById(R.id.cartRecyclerView)
        tvTotalPrice = findViewById(R.id.totalPrice)
        tvItemCount = findViewById(R.id.tvItemCount)
        btnContinueShoppingBottom = findViewById(R.id.btnContinueShoppingBottom)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        emptyCartView = findViewById(R.id.emptyCartView)
        btnContinueShopping = findViewById(R.id.btnContinueShopping)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(this, cartList, {}, ::removeItem)
        recyclerView.adapter = cartAdapter

        // Setup Navigation Drawer
        setupNavigationDrawer()

        // Load saved cart
        cartList.addAll(CartStorage.getCart(this))
        cartAdapter.notifyDataSetChanged()
        updateCartUI()

        // Handle Intent data (if item added from product page)
        val cartItem = intent.getParcelableExtra<CartItem>("cartItem")
        cartItem?.let {
            cartList.add(it)
            cartAdapter.notifyDataSetChanged()
            CartStorage.saveCart(this, cartList) // Save updated cart
            updateCartUI()
        }

        // Back button listener
        toolbar.setNavigationOnClickListener {
            vibrate()
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        btnContinueShopping.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }

       btnContinueShoppingBottom.setOnClickListener {
           val intent = Intent(this, Homepage::class.java)
           startActivity(intent)
           overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
           finish()
       }

        btnPlaceOrder.setOnClickListener {
            val selectedItems = cartAdapter.getSelectedItems()
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Please select at least one item to place an order.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putParcelableArrayListExtra("selectedItems", ArrayList(selectedItems))
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
        }
    }

    // Updates UI based on cart contents
    private fun updateCartUI() {
        val totalPrice = cartList.sumOf { it.productPrice * it.quantity }
        tvTotalPrice.text = "Total: ₱%.2f".format(totalPrice)
        tvItemCount.text = "(${cartList.size} items)"

        if (cartList.isEmpty()) {
            emptyCartView.visibility = LinearLayout.VISIBLE
            recyclerView.visibility = LinearLayout.GONE
        } else {
            emptyCartView.visibility = LinearLayout.GONE
            recyclerView.visibility = LinearLayout.VISIBLE
        }
    }

    private fun setupNavigationDrawer() {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, Homepage::class.java))
            }
            drawerLayout.closeDrawers()
            true
        }

        toolbar.setNavigationOnClickListener {
            vibrate()
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun removeItem(cartItem: CartItem) {
        cartList.remove(cartItem)
        cartAdapter.notifyDataSetChanged()
        CartStorage.saveCart(this, cartList) // Save updated cart
        updateCartUI()
        Toast.makeText(this, "Removed item from cart", Toast.LENGTH_SHORT).show()
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
