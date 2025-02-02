package com.rendonapp.thriftique

import Authentication.LogIn
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.SignUp
import com.rendonapp.thriftique.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up click listeners for both LogIn and SignUp
        setUpClickListener(R.id.LogIn, "Log In clicked", LogIn::class.java)
        setUpClickListener(R.id.signUp, "Sign Up clicked", SignUp::class.java)
    }

    // Function to set up click listeners for a given LinearLayout ID, message, and target activity
    private fun setUpClickListener(viewId: Int, message: String, targetActivity: Class<*>) {
        val layout: LinearLayout = findViewById(viewId)
        layout.setOnClickListener {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, targetActivity)
            startActivity(intent)

            // Apply transition animation when navigating to the target activity
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Add fade-in and fade-out animation
        }
    }
}
