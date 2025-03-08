package api

import android.content.Context
import android.content.SharedPreferences

object Constants {
    private const val PREFS_NAME = "app_prefs"
    private const val BASE_URL_KEY = "base_url"

    // Default Base URL
    private const val DEFAULT_BASE_URL = "http://192.168.100.184/thriftique_db/includes/v1/Products/uploads/"

    fun getBaseUrl(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(BASE_URL_KEY, DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
    }

    fun setBaseUrl(context: Context, newUrl: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(BASE_URL_KEY, newUrl).apply()
    }
}
