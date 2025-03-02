package com.rendonapp.thriftique

import Authentication.LogIn
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity

fun logoutUser(context: Context) {
    val alertDialog = AlertDialog.Builder(context)
    alertDialog.setTitle("Logout")
    alertDialog.setMessage("Are you sure you want to log out?")

    alertDialog.setPositiveButton("Yes") { _, _ ->
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Clear session data
        editor.apply()

        // Redirect to Login Activity
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(context, intent, null)

        // If context is an Activity, finish it
        if (context is Homepage) {
            context.finish()
        }
    }

    alertDialog.setNegativeButton("Cancel") { dialog, _ ->
        dialog.dismiss() // Close dialog if user cancels
    }

    alertDialog.show()
}
