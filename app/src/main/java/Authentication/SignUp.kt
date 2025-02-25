package com.example.android

import ApiService
import Authentication.LogIn
import Authentication.RegisterPart2
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.android.models.ApiResponse
import com.example.android.models.RegisterPart1
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

        // Navigation
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            vibrateDevice(50)
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


    private fun updateProgressView(step: Int) {
        val step1 = findViewById<View>(R.id.step1)
        val step2 = findViewById<View>(R.id.step2)
        val progressLine = findViewById<View>(R.id.progress_line)

        when (step) {
            1 -> {
                step1.setBackgroundResource(R.drawable.progress_active)
                progressLine.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
                step2.setBackgroundResource(R.drawable.progress_inactive)
            }

            2 -> {
                step1.setBackgroundResource(R.drawable.progress_active)
                progressLine.setBackgroundColor(ContextCompat.getColor(this, R.color.active))
                step2.setBackgroundResource(R.drawable.progress_active)
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
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val user = RegisterPart1(firstname, lastname, email, password, confirmPassword)

        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        apiService.registerUser(user).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    Log.d("SignUp", "Step 1 successful: ${response.body()?.message}")
                    Toast.makeText(
                        this@SignUp,
                        "Step 1 Complete! Proceed to Step 2",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Proceed to Step 2 (RegisterPart2)
                    val intent = Intent(this@SignUp, RegisterPart2::class.java)
                    intent.putExtra("email", email) // Pass email to Step 2
                    startActivity(intent)
                } else {
                    Log.e("SignUp", "Step 1 failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("SignUp", "Step 1 network error: ${t.message}")
            }
        })
    }
}
