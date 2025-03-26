import Authentication.LogIn
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

fun logoutUser(context: Context) {
    val alertDialog = AlertDialog.Builder(context)
    alertDialog.setTitle("Logout")
    alertDialog.setMessage("Are you sure you want to log out?")

    alertDialog.setPositiveButton("Yes") { _, _ ->
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        // Preserve user orders (DO NOT REMOVE OrdersPref)
        if (userId != -1) {
            val ordersPref = context.getSharedPreferences("OrdersPref", Context.MODE_PRIVATE)
            val userOrders = ordersPref.getString("orders_$userId", null)

            // Save orders before clearing session
            val tempOrders = userOrders ?: ""

            // Clear session data only (not orders)
            sharedPreferences.edit().apply {
                clear()
                apply()
            }

            // Restore orders for this user
            ordersPref.edit().putString("orders_$userId", tempOrders).apply()
        }

        // Redirect to Login Activity with proper cleanup
        val intent = Intent(context, LogIn::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)

        // If context is an Activity, finish it
        if (context is AppCompatActivity) {
            context.finishAffinity()
        }
    }

    alertDialog.setNegativeButton("Cancel") { dialog, _ ->
        dialog.dismiss()
    }

    alertDialog.show()
}
