package com.rendonapp.thriftique


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LogIn : AppCompatActivity() {
    // Declare variables for views
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var forgotPassword: TextView
    private lateinit var logInBtn: Button
    private lateinit var newAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // Initialize views
        initializeViews()

        // Set up click listeners
        setupListeners()
    }

    // Function to initialize views
    private fun initializeViews() {
        usernameLayout = findViewById(R.id.username)
        passwordLayout = findViewById(R.id.password)
        usernameEditText = usernameLayout.editText as TextInputEditText
        passwordEditText = passwordLayout.editText as TextInputEditText
        forgotPassword = findViewById(R.id.forgotPassword)
        logInBtn = findViewById(R.id.LogInBtn)
        newAccount = findViewById(R.id.newAccount)
    }

    // Function to set up click listeners
    private fun setupListeners() {
        // Handle login button click
        logInBtn.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Clear errors
            usernameLayout.error = null
            passwordLayout.error = null

            // Check for empty fields
            if (username.isEmpty()) {
                usernameLayout.error = "Please enter your username"
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                usernameLayout.error = "Please enter a valid email address"
                usernameEditText.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        usernameLayout.error = "Please enter a valid email address"
                    }
            }
            if (password.isEmpty()) {
                passwordLayout.error = "Please enter your password"
            }else if(password.length < 8){
                passwordLayout.error = "Password must be at least 8 characters"
            }

            // Proceed if no errors
          if (usernameLayout.error == null && passwordLayout.error == null && username.isNotEmpty() && password.isNotEmpty()){
              val intent = Intent(this, MainActivity::class.java)
              startActivity(intent)
          }
        }

        // Handle forgot password click
        forgotPassword.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // Replace with your Forgot Password activity
            startActivity(intent)
        }

        // Handle new account creation click
        newAccount.setOnClickListener {
            val intent = Intent(this, SignUp::class.java) // Replace with your Sign Up activity
            startActivity(intent)
        }
    }
}
}
