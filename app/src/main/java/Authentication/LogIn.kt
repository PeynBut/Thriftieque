package Authentication

import LoginRequest
import LoginResponse
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.SignUp
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rendonapp.thriftique.Homepage
import com.rendonapp.thriftique.MainActivity
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogIn : AppCompatActivity() {
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var forgotPassword: TextView
    private lateinit var logInBtn: Button
    private lateinit var newAccount: TextView
    private lateinit var textView: TextView
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // Initialize views
        initializeViews()

        // Apply gradient to TextView (Login text)
        applyGradientToText()

        // Set up click listeners
        setupListeners()
    }

    private fun initializeViews() {
        usernameLayout = findViewById(R.id.username)
        passwordLayout = findViewById(R.id.password)
        usernameEditText = usernameLayout.editText as TextInputEditText
        passwordEditText = passwordLayout.editText as TextInputEditText
        forgotPassword = findViewById(R.id.forgotPassword)
        logInBtn = findViewById(R.id.LogInBtn)
        newAccount = findViewById(R.id.newAccount)
        backBtn = findViewById(R.id.backbtn)
        textView = findViewById(R.id.textView)
    }

    private fun applyGradientToText() {
        val paint = textView.paint
        val textWidth = paint.measureText(textView.text.toString())
        val shader = LinearGradient(
            0f, 0f, textWidth, textView.textSize,
            Color.parseColor("#71879D"), Color.parseColor("#FFE87C"), Shader.TileMode.CLAMP
        )
        paint.shader = shader
    }

    private fun setupListeners() {
        logInBtn.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            usernameLayout.error = null
            passwordLayout.error = null

            if (username.isEmpty()) {
                usernameLayout.error = "Please enter your email"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                usernameLayout.error = "Please enter a valid email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordLayout.error = "Please enter your password"
                return@setOnClickListener
            }

            // Call Retrofit login function
            loginUser(username, password)
        }

        newAccount.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun loginUser(email: String, password: String) {
        val apiService = RetrofitClient.instance.loginUser(LoginRequest(email, password))

        apiService.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LoginDebug", "Response Code: ${response.code()}")
                Log.d("LoginDebug", "Raw Response: ${response.raw()}")

                val responseBody = response.body()
                val errorBody = response.errorBody()?.string()

                if (response.isSuccessful && responseBody != null) {
                    Log.d("LoginDebug", "Response Body: $responseBody")

                    // ✅ Fix: Check if `id` and `token` exist instead of `status`
                    if (responseBody.id != null && responseBody.token != null) {
                        Toast.makeText(this@LogIn, "Login Successful", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LogIn, Homepage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // ✅ Handle missing fields properly
                        val errorMessage = responseBody.message ?: "Invalid Credentials"
                        Toast.makeText(this@LogIn, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginError", "Server Error: $errorBody")
                    Toast.makeText(this@LogIn, "Server Error: $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginError", "Network error: ${t.message}", t)
                Toast.makeText(this@LogIn, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
