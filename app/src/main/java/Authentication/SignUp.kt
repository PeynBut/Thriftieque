package com.example.android

import ApiResponse
import ApiService
import Authentication.LogIn
import User
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.rendonapp.thriftique.MainActivity
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var et_firstname: TextInputEditText
    private lateinit var et_lastname: TextInputEditText
    private lateinit var et_email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var signUpBtn: Button
    private lateinit var alreadyHaveAccount: TextView
    private lateinit var textView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize views
        backBtn = findViewById(R.id.backbtn)
        et_firstname = findViewById(R.id.et_firstname_edit)
        et_lastname = findViewById(R.id.et_lastname_edit)
        et_email = findViewById(R.id.et_email_edit)
        password = findViewById(R.id.password_edit)
        confirmPassword = findViewById(R.id.confirmPassword_toggle)
        signUpBtn = findViewById(R.id.LogIn_bnt)
        alreadyHaveAccount = findViewById(R.id.alrady_have_account)
        textView = findViewById(R.id.textView)

        // Apply gradient to TextView
        val shader = LinearGradient(
            0f, 0f, textView.paint.measureText(textView.text.toString()), textView.textSize,
            Color.parseColor("#71879D"), Color.parseColor("#FFE87C"), Shader.TileMode.CLAMP
        )
        textView.paint.shader = shader

        // Set up listeners
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        signUpBtn.setOnClickListener {
            if (validateInputs()) {
                val firstname = et_firstname.text.toString().trim()
                val lastname = et_lastname.text.toString().trim()
                val email = et_email.text.toString().trim()
                val pass = password.text.toString().trim()

                Log.d(
                    "SignUp",
                    "Input data - FirstName: $firstname, LastName: $lastname, Email: $email"
                )

                registerUser(firstname, lastname, email, pass)
            } else {
                Log.e("SignUp", "Validation failed")
            }
        }

        alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun validateInputs(): Boolean {
        val firstname = et_firstname.text.toString().trim()
        val lastname = et_lastname.text.toString().trim()
        val email = et_email.text.toString().trim()
        val pass = password.text.toString().trim()
        val confirmPass = confirmPassword.text.toString().trim()

        et_firstname.error = null
        et_lastname.error = null
        et_email.error = null
        password.error = null
        confirmPassword.error = null

        var isValid = true

        if (firstname.isEmpty()) {
            et_firstname.error = "Please enter your first name"
            isValid = false
        }
        if (lastname.isEmpty()) {
            et_lastname.error = "Please enter your last name"
            isValid = false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.error = "Please enter a valid email"
            isValid = false
        }
        if (pass.isEmpty() || pass.length < 8) {
            password.error = "Password must be at least 8 characters"
            isValid = false
        }
        if (confirmPass.isEmpty() || confirmPass != pass) {
            confirmPassword.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    private fun registerUser(firstname: String, lastname: String, email: String, password: String) {
        val progressDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .create()
        progressDialog.show()

        val user = User(firstname, lastname, email, password)

        Log.d("SignUp", "Registering user: $user")

        RetrofitClient.retrofit
            .create(ApiService::class.java)
            .registerUser(user)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    progressDialog.dismiss()
                    Log.e("SignUp", "Response Code: ${response.code()}") // Log HTTP code
                    Log.e("SignUp", "Response Body: ${response.body()}") // Log response data
                    Log.e("SignUp", "Error Body: ${response.errorBody()?.string()}") // Log error details

                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null && !apiResponse.error) {
                            Log.d("SignUp", "Registration successful")
                            Toast.makeText(
                                this@SignUp,
                                "Registration Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@SignUp, MainActivity::class.java))
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                            finish()
                        } else {
                            Log.e(
                                "SignUp",
                                "Registration failed: ${apiResponse?.message ?: "Unknown error"}"
                            )
                            Toast.makeText(
                                this@SignUp,
                                "Registration Failed: ${apiResponse?.message ?: "Unknown error"}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.e(
                            "RegistrationError",
                            "Server Error: ${response.code()} ${response.message()}"
                        )
                        Toast.makeText(
                            this@SignUp,
                            "Failed to register user. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.e("SignUp", "RetrofitError: ${t.message}")
                    Toast.makeText(
                        this@SignUp,
                        "Registration Failed: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
