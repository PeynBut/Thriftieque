package com.rendonapp.thriftique

import Authentication.LogIn
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import clothing.CartActivity
import com.google.android.material.appbar.MaterialToolbar
import setting.SettingsActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val btnOrders = findViewById<Button>(R.id.btnOrders)
        val btnWishlist = findViewById<Button>(R.id.btnWishlist)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // 🔙 Handle Back Button
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 🛒 Navigate to Order History
        btnOrders.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // ⚙️ Navigate to Settings
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // 🚪 Logout Functionality
        btnLogout.setOnClickListener {
            logoutUser()
        }

        // Load actual user data from SharedPreferences or Database
        val userName = sharedPreferences.getString("user_name", "Guest User")
        val userEmail = sharedPreferences.getString("user_email", "guest@email.com")

        tvUserName.text = userName
        tvEmail.text = userEmail
    }

    private fun logoutUser() {
        val editor = sharedPreferences.edit()
        editor.clear()  // Clear all stored user session data
        editor.apply()

        // Navigate to LogIn activity and clear back stack
        val intent = Intent(this, LogIn::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
