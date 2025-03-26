import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rendonapp.thriftique.CartItem

object CartStorage {
    private const val PREFS_NAME = "cart_prefs"

    fun saveCart(context: Context, userId: String, cartList: List<CartItem>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(cartList)
        editor.putString("cart_$userId", json)  // Save per user
        editor.apply()
    }

    fun getCart(context: Context, userId: String): List<CartItem> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("cart_$userId", null)
        return if (json != null) {
            val type = object : TypeToken<List<CartItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun clearCart(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("cart_$userId").apply()
    }
}
