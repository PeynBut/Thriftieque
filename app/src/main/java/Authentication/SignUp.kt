package com.example.android

import ApiService
import Authentication.LogIn
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
import com.example.android.models.ApiResponse
import com.example.android.models.User
import com.rendonapp.thriftique.MainActivity
import com.rendonapp.thriftique.R
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var etFirstname: TextInputEditText
    private lateinit var etLastname: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var etPhoneNumber: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var signUpBtn: Button
    private lateinit var alreadyHaveAccount: TextView
    private lateinit var textView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize views
        backBtn = findViewById(R.id.backbtn)
        etFirstname = findViewById(R.id.et_firstname_edit)
        etLastname = findViewById(R.id.et_lastname_edit)
        etEmail = findViewById(R.id.et_email_edit)
        etPassword = findViewById(R.id.password_edit)
        etConfirmPassword = findViewById(R.id.confirmPassword_toggle)
        etPhoneNumber = findViewById(R.id.et_phone_number_edit)
        etAddress = findViewById(R.id.et_address_edit)
        signUpBtn = findViewById(R.id.LogIn_bnt)
        alreadyHaveAccount = findViewById(R.id.already_have_account)
        textView = findViewById(R.id.textView)

        // Apply gradient to TextView
        val shader = LinearGradient(
            0f, 0f, textView.paint.measureText(textView.text.toString()), textView.textSize,
            Color.parseColor("#71879D"), Color.parseColor("#FFE87C"), Shader.TileMode.CLAMP
        )
        textView.paint.shader = shader

        // Navigation
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Register User
        signUpBtn.setOnClickListener {
            if (validateInputs()) {
                val firstname = etFirstname.text.toString().trim()
                val lastname = etLastname.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val pass = etPassword.text.toString().trim()
                val phoneNumber = etPhoneNumber.text.toString().trim()
                val userAddress = etAddress.text.toString().trim()

                Log.d("SignUp", "Registering: FirstName=$firstname, LastName=$lastname, Email=$email, Phone=$phoneNumber, Address=$userAddress")

                registerUser(firstname, lastname, email, pass, phoneNumber, userAddress)
            } else {
                Log.e("SignUp", "Validation failed")
            }
        }
    }

    private fun validateInputs(): Boolean {
        val firstname = etFirstname.text.toString().trim()
        val lastname = etLastname.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val pass = etPassword.text.toString().trim()
        val confirmPass = etConfirmPassword.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val userAddress = etAddress.text.toString().trim()

        etFirstname.error = null
        etLastname.error = null
        etEmail.error = null
        etPassword.error = null
        etConfirmPassword.error = null
        etPhoneNumber.error = null
        etAddress.error = null

        var isValid = true

        if (firstname.isEmpty()) {
            etFirstname.error = "Please enter your first name"
            isValid = false
        }
        if (lastname.isEmpty()) {
            etLastname.error = "Please enter your last name"
            isValid = false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Please enter a valid email"
            isValid = false
        }
        if (pass.isEmpty() || pass.length < 8) {
            etPassword.error = "Password must be at least 8 characters"
            isValid = false
        }
        if (confirmPass.isEmpty() || confirmPass != pass) {
            etConfirmPassword.error = "Passwords do not match"
            isValid = false
        }
        if (phoneNumber.isEmpty() || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            etPhoneNumber.error = "Please enter a valid phone number"
            isValid = false
        }
        if (userAddress.isEmpty()) {
            etAddress.error = "Please enter your address"
            isValid = false
        }

        return isValid
    }

    private fun registerUser(firstname: String, lastname: String, email: String, password: String, phoneNumber: String, address: String) {
        val progressDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .create()
        progressDialog.show()

        val user = User(firstname, lastname, email, password, phoneNumber ?: "", null, null, address)


        Log.d("SignUp", "Sending request: $user")

        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        apiService.registerUser(user).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                progressDialog.dismiss()
                Log.d("SignUp", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && !apiResponse.error) {
                        Log.d("SignUp", "Registration successful")
                        Toast.makeText(this@SignUp, "Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUp, MainActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    } else {
                        Log.e("SignUp", "Failed: ${apiResponse?.message ?: "Unknown error"}")
                        Toast.makeText(this@SignUp, "Registration Failed: ${apiResponse?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("SignUp", "Server Error: ${response.code()} ${response.message()}")
                    Toast.makeText(this@SignUp, "Failed to register user. Try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("SignUp", "Network Error: ${t.message}")
                Toast.makeText(this@SignUp, "Registration Failed: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
