package Authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.HapticFeedbackConstants
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.SignUp
import com.example.android.models.LoginRequest
import com.example.android.models.LoginResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rendonapp.thriftique.Homepage
import com.rendonapp.thriftique.MainActivity
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogIn : AppCompatActivity() {
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var forgotPassword: TextView
    private lateinit var logInBtn: MaterialButton
    private lateinit var newAccount: TextView
    private lateinit var backBtn: ImageView
    private lateinit var textView: TextView
    private lateinit var vibrator: Vibrator
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in) // Move this above everything

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        // Initialize Vibrator
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            startActivity(Intent(this, Homepage::class.java))
            finish()
            return  // Prevents further execution
        }

        // Initialize views
        initializeViews()

        // Apply gradient to TextView (Login text)
        applyGradientToText()

        // Set up click listeners
        setupListeners()
    }

    // Function to check if vibration is enabled
    private fun isVibrationEnabled(): Boolean {
        return sharedPreferences.getBoolean("vibration", true) // Default: ON
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    private fun initializeViews() {
        emailLayout = findViewById(R.id.emailLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
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
        logInBtn.setOnClickListener { view ->
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            if (isVibrationEnabled()) vibrate() // Check setting before vibrating
            validateAndLogin()
        }

        newAccount.setOnClickListener {
            if (isVibrationEnabled()) vibrate()
            startActivity(Intent(this, SignUp::class.java))
        }

        backBtn.setOnClickListener {
            if (isVibrationEnabled()) vibrate()
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        forgotPassword.setOnClickListener {
            if (isVibrationEnabled()) vibrate()
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateAndLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        emailLayout.error = null
        passwordLayout.error = null

        if (email.isEmpty()) {
            emailLayout.error = "Please enter your email"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Please enter a valid email"
            return
        }
        if (password.isEmpty()) {
            passwordLayout.error = "Please enter your password"
            return
        }

        loginUser(email, password)
    }

    private fun loginUser(email: String, password: String) {
        val apiService = RetrofitClient.instance.loginUser(LoginRequest(email, password))

        apiService.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LoginDebug", "Response Code: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    Log.d("LoginDebug", "Response Body: $responseBody")

                    if (responseBody?.id != null && responseBody.token != null) {
                        saveUserSession(responseBody.id, responseBody.token)

                        Toast.makeText(this@LogIn, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LogIn, Homepage::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LogIn, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginError", "Server Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@LogIn, "Invalid email or password", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginError", "Network error: ${t.message}", t)
                Toast.makeText(this@LogIn, "Network error. Please try again.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveUserSession(userId: Int, token: String) {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("user_id", userId)
        editor.putString("token", token)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

    private fun vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }
}
