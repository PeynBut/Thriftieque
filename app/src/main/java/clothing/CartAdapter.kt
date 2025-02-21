package clothing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rendonapp.thriftique.CartItem
import com.rendonapp.thriftique.R

class CartAdapter(
    private val context: Context,
    private var itemList: List<CartItem>,
    private val itemClickListener: (CartItem) -> Unit, // Click listener for item clicks
    private val removeClickListener: (CartItem) -> Unit // Click listener for remove actions
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productIdTextView: TextView = itemView.findViewById(R.id.label)
        private val quantityTextView: TextView = itemView.findViewById(R.id.cartItemQuantity)
        private val removeButton: Button = itemView.findViewById(R.id.btnRemove)

        fun bind(cartItem: CartItem) {
            productIdTextView.text = "Product ID: ${cartItem.productId}"
            quantityTextView.text = "Quantity: ${cartItem.quantity}"

            // Set up item click listener
            itemView.setOnClickListener {
                itemClickListener(cartItem) // Trigger the item click action
            }

            // Set up the remove button click listener
            removeButton.setOnClickListener {
                removeClickListener(cartItem) // Trigger the remove action
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = itemList[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // Method to update the item list
    fun updateItems(newItems: List<CartItem>) {
        itemList = newItems
        notifyDataSetChanged()
    }
}
