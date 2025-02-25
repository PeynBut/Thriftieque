package com.rendonapp.thriftique



import android.annotation.SuppressLint
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
    // Variables
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var image: ImageView
    private lateinit var textView: TextView
    private lateinit var slogan: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen) // Replace with your splash layout file

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

        // Transition to MainActivity after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish() // Close this activity to prevent going back to it
        }, 3000) // 3000 ms = 3 seconds
    }
}
