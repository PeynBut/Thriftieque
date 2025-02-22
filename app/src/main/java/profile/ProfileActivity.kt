package profile


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.rendonapp.thriftique.R
import com.rendonapp.thriftique.orders.OrderHistoryActivity
import com.rendonapp.thriftique.wishlist.WishlistActivity
import com.rendonapp.thriftique.settings.SettingsActivity

class ProfileActivity : AppCompatActivity() {

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

        // 🔙 Handle Back Button
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 🛒 Navigate to Order History
        btnOrders.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        // ❤️ Navigate to Wishlist
        btnWishlist.setOnClickListener {
            startActivity(Intent(this, WishlistActivity::class.java))
        }

        // ⚙️ Navigate to Settings
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // 🚪 Logout
        btnLogout.setOnClickListener {
            // Perform logout logic (clear user data, navigate to login)
            finish()
        }

        // TODO: Load actual user data from SharedPreferences or Database
        tvUserName.text = "John Doe"
        tvEmail.text = "john.doe@email.com"
    }
}
