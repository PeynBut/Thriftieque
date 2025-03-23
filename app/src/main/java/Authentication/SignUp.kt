package com.example.android

import ApiService
import Authentication.LogIn
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.android.models.SignUpResponse
import com.google.android.material.textfield.TextInputEditText
import com.rendonapp.thriftique.MainActivity
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Vibrator
import android.os.VibrationEffect
import android.content.Context

class SignUp : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var etFirstname: TextInputEditText
    private lateinit var etLastname: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
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
        signUpBtn = findViewById(R.id.LogIn_bnt)
        alreadyHaveAccount = findViewById(R.id.already_have_account)
        textView = findViewById(R.id.textView)

        // Apply gradient to TextView
        val shader = LinearGradient(
            0f, 0f, textView.paint.measureText(textView.text.toString()), textView.textSize,
            Color.parseColor("#71879D"), Color.parseColor("#FFE87C"), Shader.TileMode.CLAMP
        )
        textView.paint.shader = shader

        // Back button click
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            vibrateDevice(50)
        }

        // Sign-up button click
        signUpBtn.setOnClickListener {
            if (validateInputs()) {
                val firstname = etFirstname.text.toString().trim()
                val lastname = etLastname.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val pass = etPassword.text.toString().trim()
                val confirmpass = etConfirmPassword.text.toString().trim()

                Log.d(
                    "SignUp",
                    "Registering: FirstName=$firstname, LastName=$lastname, Email=$email"
                )

                registerUser(firstname, lastname, email, pass, confirmpass)
            } else {
                Log.e("SignUp", "Validation failed")
            }
        }

        // Navigate to login
        alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun vibrateDevice(duration: Long = 100) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) { // Check if the device supports vibration
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        duration,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(duration)
            }
        }
    }

    private fun validateInputs(): Boolean {
        val firstname = etFirstname.text.toString().trim()
        val lastname = etLastname.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val pass = etPassword.text.toString().trim()
        val confirmPass = etConfirmPassword.text.toString().trim()

        etFirstname.error = null
        etLastname.error = null
        etEmail.error = null
        etPassword.error = null
        etConfirmPassword.error = null

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
        } else if (!email.endsWith("@gmail.com")) {
            etEmail.error = "Only Gmail addresses are allowed"
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

        if (!isValid) {
            vibrateDevice(200) // Vibrate for 200ms on validation error
        }

        return isValid
    }

    private fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val request = com.example.android.models.SignUp(firstName, lastName, email, password, confirmPassword)

        RetrofitClient.instance.signup(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                Log.d("SignUp", "Raw Response: ${response.raw()}") // Log raw response
                Log.d("SignUp", "Response Code: ${response.code()}") // Log response code
                Log.d("SignUp", "Response Body: ${response.body()}") // Log response body

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!
                    if (result.error == null || result.error == false) {  // ✅ Compare with Boolean 'false'
                        Toast.makeText(this@SignUp, "Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUp, LogIn::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignUp, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUp, "Server Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(
                    this@SignUp,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
