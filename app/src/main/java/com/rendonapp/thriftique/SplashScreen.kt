package com.rendonapp.thriftique

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var image: ImageView
    private lateinit var textView: TextView
    private lateinit var slogan: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Load Animations
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_anim)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.animation)

        // Hook Views
        image = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        slogan = findViewById(R.id.slogan)

        // Apply Animations
        slogan.animation = bottomAnimation
        image.animation = topAnimation
        textView.animation = bottomAnimation

        // Delay transition
        Handler(Looper.getMainLooper()).postDelayed({
            if (isUserLoggedIn()) {
                startActivity(Intent(this, Homepage::class.java)) // Stay on Homepage if logged in
            } else {
                startActivity(Intent(this, MainActivity::class.java)) // Go to MainActivity if not logged in
            }
            finish() // Close this activity
        }, 3000) // 3 seconds delay
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
}
