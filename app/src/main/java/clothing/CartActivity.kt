package clothing

import Authentication.LogIn
import Products.CheckoutActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
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
    private lateinit var userId: String
    private lateinit var backbtn : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        userId = getLoggedInUserId()
        if (userId.isEmpty()) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LogIn::class.java))
            finish()
            return
        }

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
        backbtn = findViewById(R.id.btnBack)

        setupRecyclerView()
        setupNavigationDrawer()

        // Load user's cart
        cartList.addAll(CartStorage.getCart(this, userId))
        cartAdapter.notifyDataSetChanged()
        updateCartUI()

        // Handle Intent data (if item added from product page)
        intent.getParcelableExtra<CartItem>("cartItem")?.let { newItem ->
            val existingItem = cartList.find { it.productId == newItem.productId }
            if (existingItem != null) {
                existingItem.quantity += newItem.quantity
            } else {
                cartList.add(newItem)
            }
            cartAdapter.notifyDataSetChanged()
            CartStorage.saveCart(this, userId, cartList)
            updateCartUI()
        }

        toolbar.setNavigationOnClickListener {
            vibrate()
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer if it's open
            } else {
                onBackPressedDispatcher.onBackPressed() // Navigate back if the drawer is closed
            }
        }

        backbtn.setOnClickListener {
            vibrate()
            onBackPressedDispatcher.onBackPressed()
        }


        btnContinueShopping.setOnClickListener { navigateToHomepage() }
        btnContinueShoppingBottom.setOnClickListener { navigateToHomepage() }
        btnPlaceOrder.setOnClickListener { placeOrder() }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(this, cartList, {}, ::removeItem)
        recyclerView.adapter = cartAdapter
    }

    private fun navigateToHomepage() {
        startActivity(Intent(this, Homepage::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun placeOrder() {
        val selectedItems = cartAdapter.getSelectedItems()
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Please select at least one item to place an order.", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putParcelableArrayListExtra("selected_items", ArrayList(selectedItems))
            startActivity(intent)

            cartList.removeAll(selectedItems)
            cartAdapter.notifyDataSetChanged()
            CartStorage.saveCart(this, userId, cartList)
            updateCartUI()
        }
    }

    private fun updateCartUI() {
        val totalPrice = cartList.sumOf { it.productPrice * it.quantity }
        tvTotalPrice.text = "Total: ₱%.2f".format(totalPrice)
        tvItemCount.text = "(${cartList.size} items)"

        if (cartList.isEmpty()) {
            emptyCartView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyCartView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun setupNavigationDrawer() {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navigateToHomepage()
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
        val position = cartList.indexOf(cartItem)
        if (position != -1) {
            cartList.removeAt(position)
            cartAdapter.notifyItemRemoved(position)
            CartStorage.saveCart(this, userId, cartList)
            updateCartUI()
            Toast.makeText(this, "Removed item from cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun getLoggedInUserId(): String {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1).takeIf { it != -1 }?.toString() ?: ""
    }
}
