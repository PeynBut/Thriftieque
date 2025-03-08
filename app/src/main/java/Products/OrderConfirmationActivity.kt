package Products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rendonapp.thriftique.Homepage
import com.rendonapp.thriftique.R

class OrderConfirmationActivity : AppCompatActivity() {

    private lateinit var tvConfirmationMessage: TextView
    private lateinit var tvPaymentDetails: TextView
    private lateinit var btnBackToHome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        tvConfirmationMessage = findViewById(R.id.tvConfirmationMessage)
        tvPaymentDetails = findViewById(R.id.tvPaymentDetails)
        btnBackToHome = findViewById(R.id.btnBackToHome)

        // Retrieve payment details
        val paymentMethod = intent.getStringExtra("paymentMethod") ?: "Unknown"
        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)

        tvConfirmationMessage.text = "Your order has been placed successfully!"
        tvPaymentDetails.text = "Payment Method: $paymentMethod\nTotal Paid: $$totalAmount"

        btnBackToHome.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
