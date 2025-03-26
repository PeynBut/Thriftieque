package Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.rendonapp.thriftique.R


class ForgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass)

        val backBtn: ImageView = findViewById(R.id.backbtn)
        val emailField: TextInputEditText = findViewById(R.id.etEmail)
        val resetPasswordBtn: MaterialButton = findViewById(R.id.resetPasswordBtn)

        backBtn.setOnClickListener {
            finish() // Go back to the previous screen
        }

        resetPasswordBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                sendPasswordResetEmail(email)
            }
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        // Implement your password reset logic here
        Toast.makeText(this, "Password reset link sent to $email", Toast.LENGTH_LONG).show()
    }
}
