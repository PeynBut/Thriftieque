package clothing

import Products.CheckoutActivity
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
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
    private lateinit var btnContinueShoppingBottom: MaterialButton
    private lateinit var btnPlaceOrder: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize UI elements
        toolbar = findViewById(R.id.topAppBar)
        drawerLayout = findViewById(R.id.drawer_layout)
        recyclerView = findViewById(R.id.cartRecyclerView)
        tvTotalPrice = findViewById(R.id.totalPrice)
        btnContinueShoppingBottom = findViewById(R.id.btnContinueShoppingBottom)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)


        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(this, cartList, {}, ::removeItem)
        recyclerView.adapter = cartAdapter

        // Setup Navigation Drawer
        setupNavigationDrawer()

        // Back button listener
        toolbar.setNavigationOnClickListener {
            vibrate()
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        val cartItem = intent.getParcelableExtra<CartItem>("cartItem")
        cartItem?.let {
            cartList.add(it)
            cartAdapter.notifyDataSetChanged()
            calculateTotalPrice()
            CartStorage.saveCart(this, cartList) // Save the cart
        }
        btnContinueShoppingBottom.setOnClickListener {
            startActivity(Intent(this, Homepage::class.java))
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
    private fun calculateTotalPrice() {
        val totalPrice = cartList.sumOf { it.productPrice * it.quantity }
        tvTotalPrice.text = "Total: ₱%.2f".format(totalPrice)
    }


    private fun setupNavigationDrawer() {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
        cartList.addAll(CartStorage.getCart(this)) // Load saved cart
        cartAdapter.notifyDataSetChanged()
        calculateTotalPrice()

    }

    private fun removeItem(cartItem: CartItem) {
        val index = cartList.indexOf(cartItem)
        if (index != -1) {
            cartList.removeAt(index)
            cartAdapter.notifyItemRemoved(index)
            calculateTotalPrice()
            CartStorage.saveCart(this, cartList) // Save the cart
            Toast.makeText(this, "Removed item from cart", Toast.LENGTH_SHORT).show()
        }

        if (cartList.isEmpty()) {
            tvTotalPrice.text = "Total: ₱0.00"
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
