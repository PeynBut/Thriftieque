package Products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import api.OrderApiService
import api.OrderRequest
import api.OrderResponse
import com.rendonapp.thriftique.CartItem
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {

    private lateinit var selectedItems: ArrayList<CartItem>
    private var totalPrice: Double = 0.0
    private lateinit var tvPaymentTotal: TextView
    private lateinit var rgPaymentMethods: RadioGroup
    private lateinit var btnPayNow: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize UI elements
        tvPaymentTotal = findViewById(R.id.tvPayment_Total)
        rgPaymentMethods = findViewById(R.id.rgPaymentMethods)
        btnPayNow = findViewById(R.id.btnConfirmPayment)

        // Retrieve selected items and total price from intent
        selectedItems = intent.getParcelableArrayListExtra("selected_items") ?: arrayListOf()
        totalPrice = intent.getDoubleExtra("total_price", 0.0)

        // Display total price with formatting
        tvPaymentTotal.text = "Total: ₱%.2f".format(totalPrice)

        // Set up payment button click
        btnPayNow.setOnClickListener { processPayment() }
    }

    private fun processPayment() {
        val selectedPaymentMethodId = rgPaymentMethods.checkedRadioButtonId
        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = when (selectedPaymentMethodId) {
            R.id.rbCashOnDelivery -> "Cash on Delivery"
            else -> "Unknown"
        }

        // Retrieve user ID from shared preferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulate payment success before sending order
        Toast.makeText(this, "Processing $paymentMethod payment of ₱%.2f".format(totalPrice), Toast.LENGTH_LONG).show()

        val apiService = RetrofitClient.orderInstance


        // Send each item as a separate order request
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
                            Toast.makeText(this@PaymentActivity, "Order placed: ${orderResponse.message}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@PaymentActivity, "Order failed: ${orderResponse?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(this@PaymentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Navigate to Order Confirmation screen
        val intent = Intent(this, OrderConfirmationActivity::class.java).apply {
            putExtra("paymentMethod", paymentMethod)
            putExtra("totalAmount", totalPrice)
            putParcelableArrayListExtra("selected_items", selectedItems)
        }
        startActivity(intent)
        finish()
    }
}
