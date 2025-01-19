package com.rendonapp.thriftique

import android.annotation.SuppressLint
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
    private lateinit var et_firstname: TextInputEditText
    private lateinit var et_lastname: TextInputEditText
    private lateinit var et_email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var logIn_bnt: Button
    private lateinit var alreadyHaveAccount: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        // Initialize views
        backBtn = findViewById(R.id.backbtn)
        et_firstname = findViewById(R.id.et_firstname_edit)
        et_lastname = findViewById(R.id.et_lastname_edit)
        et_email = findViewById(R.id.et_email_edit)
        password = findViewById(R.id.password_edit)
        confirmPassword = findViewById(R.id.confirmPassword_toggle)
        logIn_bnt = findViewById(R.id.LogIn_bnt)
        alreadyHaveAccount = findViewById(R.id.alrady_have_account)

        // Set up listeners
        backBtn.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        logIn_bnt.setOnClickListener {
            val firstname = et_firstname.text.toString()
            val lastname = et_lastname.text.toString()
            val email = et_email.text.toString()
            val pass = password.text.toString()
            val confirmPass = confirmPassword.text.toString()

            // Clear previous errors
            et_firstname.error = null
            et_lastname.error = null
            et_email.error = null
            password.error = null
            confirmPassword.error = null

            var isValid = true

            // Validation
            if (firstname.isEmpty()) {
                et_firstname.error = "Please enter your first name"
                isValid = false
            }
            if (lastname.isEmpty()) {
                et_lastname.error = "Please enter your last name"
                isValid = false
            }
            if (email.isEmpty()) {
                et_email.error = "Please enter your email"
                isValid = false
            }
            if (pass.isEmpty()) {
                password.error = "Please enter your password"
                isValid = false
            }
            if (confirmPass.isEmpty()) {
                confirmPassword.error = "Please confirm your password"
                isValid = false
            }
            if (pass != confirmPass) {
                confirmPassword.error = "Passwords do not match"
                isValid = false
            }

            // Proceed only if all fields are valid
            if (isValid) {
                val intent = Intent(this, LogIn::class.java)
                startActivity(intent)
            }
        }

        alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
