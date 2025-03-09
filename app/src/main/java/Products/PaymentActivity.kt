package Products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rendonapp.thriftique.R

class PaymentActivity : AppCompatActivity() {

    private lateinit var tvPaymentTotal: TextView
    private lateinit var rgPaymentMethods: RadioGroup
    private lateinit var btnConfirmPayment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        tvPaymentTotal = findViewById(R.id.tvPayment_Total)
        rgPaymentMethods = findViewById(R.id.rgPaymentMethods)
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)

        // Retrieve total price from checkout
        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)
        tvPaymentTotal.text = "Total: ₱$totalAmount"

        btnConfirmPayment.setOnClickListener {
            processPayment(totalAmount)
        }
    }

    private fun processPayment(totalAmount: Double) {
        val selectedPaymentMethodId = rgPaymentMethods.checkedRadioButtonId

        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = when (selectedPaymentMethodId) {
            R.id.rbCashOnDelivery -> "Cash on Delivery"
            else -> "Unknown"
        }

        Toast.makeText(this, "Processing $paymentMethod payment of ₱$totalAmount", Toast.LENGTH_LONG).show()

        // Navigate to Order Confirmation screen
        val intent = Intent(this, OrderConfirmationActivity::class.java)
        intent.putExtra("paymentMethod", paymentMethod)
        intent.putExtra("totalAmount", totalAmount)
        startActivity(intent)
        finish()
    }
}
