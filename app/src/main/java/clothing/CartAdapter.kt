package clothing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.models.CartItem
import com.rendonapp.thriftique.R

class CartAdapter(
    private val context: Context,
    private val cartList: List<CartItem>,
    private val onRemoveClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder class that holds references to UI components for each cart item
    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.cartItemName)
        val productQuantity: TextView = view.findViewById(R.id.cartItemQuantity)
        val removeButton: Button = view.findViewById(R.id.btnRemove)

        init {
            // Set click listener on the remove button
            removeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRemoveClick(cartList[position])  // Trigger the remove function
                }
            }
        }
    }

    // Inflate the item layout (cart_item.xml) for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    // Bind data to the ViewHolder for each cart item
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.productName.text = "Product ID: ${cartItem.productId}"  // Displaying the product ID
        holder.productQuantity.text = "Quantity: ${cartItem.userId}"  // Displaying user ID (or implement another logic for quantity)
    }


    // Return the number of items in the cart list
    override fun getItemCount(): Int = cartList.size
}
