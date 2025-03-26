package Products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.OrderApiService
import api.OrderRequest
import api.OrderResponse
import com.rendonapp.thriftique.CartItem
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private lateinit var rvCheckoutItems: RecyclerView
    private lateinit var tvTotalAmount: TextView
    private lateinit var btnProceedToPayment: Button
    private lateinit var btnConfirmPayment: Button
    private lateinit var paymentSection: View
    private lateinit var radioGroup: RadioGroup
    private lateinit var checkoutAdapter: CheckoutAdapter
    private lateinit var selectedItems: ArrayList<CartItem>
    private var totalPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Initialize UI elements
        rvCheckoutItems = findViewById(R.id.rvCheckout_Items)
        tvTotalAmount = findViewById(R.id.tvCheckout_Total)
        btnProceedToPayment = findViewById(R.id.btnProceedTo_Payment)
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)
        paymentSection = findViewById(R.id.paymentSection)
        radioGroup = findViewById(R.id.rgPaymentMethods)

        // Retrieve selected items from intent
        selectedItems = intent.getParcelableArrayListExtra("selected_items") ?: arrayListOf()
        totalPrice = selectedItems.sumOf { it.productPrice * it.quantity }
        tvTotalAmount.text = "Total: ₱%.2f".format(totalPrice)

        // Setup RecyclerView
        checkoutAdapter = CheckoutAdapter(this, selectedItems)
        rvCheckoutItems.layoutManager = LinearLayoutManager(this)
        rvCheckoutItems.adapter = checkoutAdapter

        // Handle Proceed to Payment click
        btnProceedToPayment.setOnClickListener {
            paymentSection.visibility = View.VISIBLE
            btnProceedToPayment.visibility = View.GONE
        }

        // Handle Confirm Payment click
        btnConfirmPayment.setOnClickListener { processPayment() }
    }

    private fun processPayment() {
        val selectedPaymentMethodId = radioGroup.checkedRadioButtonId
        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = when (selectedPaymentMethodId) {
            R.id.rbCashOnDelivery -> "Cash on Delivery"
            else -> "Unknown"
        }

        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Processing $paymentMethod payment of ₱%.2f".format(totalPrice), Toast.LENGTH_LONG).show()
        val apiService = RetrofitClient.orderInstance

        selectedItems.forEach { item ->
            val orderRequest = OrderRequest(
                user_id = userId,
                product_id = item.productId,
                quantity = item.quantity,
                total_price = item.productPrice * item.quantity,
                phone = "1234567890" // Replace with actual user phone
            )

            apiService.createOrder(orderRequest).enqueue(object : Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                    if (response.isSuccessful) {
                        val orderResponse = response.body()
                        if (orderResponse != null && !orderResponse.error) {
                            Toast.makeText(this@CheckoutActivity, "Order placed: ${orderResponse.message}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@CheckoutActivity, "Order failed: ${orderResponse?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(this@CheckoutActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val intent = Intent(this, OrderConfirmationActivity::class.java).apply {
            putExtra("paymentMethod", paymentMethod)
            putExtra("totalAmount", totalPrice)
            putParcelableArrayListExtra("selected_items", selectedItems)
        }
        startActivity(intent)
        finish()
    }
}
