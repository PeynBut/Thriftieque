package Authentication

import ApiResponse
import api.ApiService
import User
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import api.RetrofitClient
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

    // Validate user inputs
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
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

    // Register user using Retrofit
    private fun registerUser(firstname: String, lastname: String, email: String, password: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering User...")
        progressDialog.show()

        // Create the user object
        val user = User(firstname, lastname, email, password)

        // Log the user data being sent
        Log.d("SignUp", "Registering user: $user")

        // Call the Retrofit API
        RetrofitClient.getClient()
            .create(ApiService::class.java)
            .registerUser(user) // Pass the user object
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    progressDialog.dismiss()

                    // Log the response
                    Log.d("SignUp", "Response: ${response.body()}")

                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse?.error == false) {
                            // Registration successful
                            Log.d("SignUp", "Registration successful")
                            Toast.makeText(this@SignUp, "Registration Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignUp, MainActivity::class.java))
                            finish()
                        } else {
                            // Registration failed
                            Log.e("SignUp", "Registration failed: ${apiResponse?.message}")
                            Toast.makeText(this@SignUp, "Registration Failed: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle unsuccessful response from the server
                        Log.e("RegistrationError", "Server Error: ${response.code()} ${response.message()}")
                        Toast.makeText(this@SignUp, "Failed to register user. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    progressDialog.dismiss()
                    // Handle failure
                    Log.e("SignUp", "RetrofitError: ${t.message}")
                    Toast.makeText(this@SignUp, "Registration Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Set up the UI and handle user interaction
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

        // Set up listeners
        backBtn.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
        }

        signUpBtn.setOnClickListener {
            if (validateInputs()) {
                val firstname = et_firstname.text.toString().trim()
                val lastname = et_lastname.text.toString().trim()
                val email = et_email.text.toString().trim()
                val pass = password.text.toString().trim()

                // Log the input data
                Log.d("SignUp", "Input data - FirstName: $firstname, LastName: $lastname, Email: $email")

                registerUser(firstname, lastname, email, pass)
            } else {
                Log.e("SignUp", "Validation failed")
            }
        }

        alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
        }
    }
}