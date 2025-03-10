package clothing

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rendonapp.thriftique.CartItem

object CartStorage {
    private const val PREF_NAME = "cart_prefs"
    private const val CART_KEY = "cart_items"

    fun saveCart(context: Context, cartList: List<CartItem>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(cartList)
        editor.putString(CART_KEY, json)
        editor.apply()
    }

    fun getCart(context: Context): MutableList<CartItem> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(CART_KEY, null)
        val type = object : TypeToken<MutableList<CartItem>>() {}.type
        return Gson().fromJson(json, type) ?: mutableListOf()
    }
}