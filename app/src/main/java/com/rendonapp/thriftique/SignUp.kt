package com.rendonapp.thriftique

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo

class SignUp : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var et_firstname: TextInputEditText
    private lateinit var et_lastname: TextInputEditText
    private lateinit var et_email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var signUpBtn: Button
    private lateinit var alreadyHaveAccount: TextView

    private fun registerUser(firstname: String, lastname: String, email: String, pass: String) {
        // Show a progress dialog while the request is in progress
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering User...")
        progressDialog.show()

        // Log the URL for debugging purposes
        Log.d("NetworkRequest", "URL: ${Constant.URL_REGISTER}")

        // StringRequest to send POST data
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            Constant.URL_REGISTER,  // The URL endpoint
            { response ->
                progressDialog.dismiss()  // Dismiss the progress dialog when the response is received
                try {
                    val jsonResponse = JSONObject(response)  // Parse the response
                    val success = jsonResponse.getBoolean("success")
                    val message = jsonResponse.getString("message")
                    Log.d("ServerResponse", "Success: $success, Message: $message")

                    // Handle the response based on success or failure
                    if (success) {
                        Toast.makeText(this, "Registration Successful: $message", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Registration Failed: $message", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing response: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressDialog.dismiss()  // Dismiss the progress dialog on error
                val errorMessage = when (error) {
                    is TimeoutError -> "Request timed out. Please try again."
                    is NoConnectionError -> "No internet connection detected. Please check your network settings."
                    is AuthFailureError -> "Authentication failed. Please check your credentials."
                    is ServerError -> "Server error. Please try again later."
                    is NetworkError -> "Network error. Please check your connection and try again."
                    is ParseError -> "Error parsing data from the server."
                    else -> "An unexpected error occurred."
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e("VolleyError", error.message ?: "Unknown error")
            }
        ) {
            override fun getParams(): Map<String, String> {
                // Add the POST data parameters (mimicking cURL data)
                return hashMapOf(
                    "firstname" to firstname,
                    "lastname" to lastname,
                    "email" to email,
                    "password" to pass
                )
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
    }




    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
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
        signUpBtn = findViewById(R.id.LogIn_bnt)  // Renamed for clarity
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

                registerUser(firstname, lastname, email, pass)
            }
        }

        alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
        }
    }
}
