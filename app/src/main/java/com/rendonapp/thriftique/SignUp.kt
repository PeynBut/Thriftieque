package com.rendonapp.thriftique

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignUp : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var firstname: TextInputEditText
    private lateinit var lastname: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var loginbtn: Button
    private lateinit var alreadyHaveAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        // Initialize views and setup listeners
        initializeViews()
        setupListeners()
    }

    // Function to initialize views
    private fun initializeViews() {
        backBtn = findViewById(R.id.backbtn)
        firstname = findViewById(R.id.firstName)
        lastname = findViewById(R.id.LastName)
        email = findViewById(R.id.Email)
        password = findViewById(R.id.Password)
        confirmPassword = findViewById(R.id.confirmPassword)
        loginbtn = findViewById(R.id.LogIn_bnt)
        alreadyHaveAccount = findViewById(R.id.alrady_have_account)
    }

    // Function to set up listeners
    private fun setupListeners() {
        // Navigate back to the login screen
        backBtn.setOnClickListener {
            navigateToLogin()
        }

        // Handle "Already have an account" text click
        alreadyHaveAccount.setOnClickListener {
            navigateToLogin()
        }

        // Handle sign-up button click
        loginbtn.setOnClickListener {
            handleSignUp()
        }
    }

    // Function to navigate to the login screen
    private fun navigateToLogin() {
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
        finish()
    }

    // Function to handle the sign-up process
    private fun handleSignUp() {
        val firstNameText = firstname.text.toString()
        val lastNameText = lastname.text.toString()
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        val confirmPasswordText = confirmPassword.text.toString()

        // Perform validation (you can expand this as needed)
        if (firstNameText.isEmpty()) {
            firstname.error = "First name is required"
        } else if (lastNameText.isEmpty()) {
            lastname.error = "Last name is required"
        } else if (emailText.isEmpty()) {
            email.error = "Email is required"
        } else if (passwordText.isEmpty()) {
            password.error = "Password is required"
        } else if (passwordText != confirmPasswordText) {
            confirmPassword.error = "Passwords do not match"
        } else {
            // Proceed with the sign-up process
            // You can add API call or database logic here
        }
    }
}
