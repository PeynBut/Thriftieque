import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.Order
import com.bumptech.glide.Glide
import com.rendonapp.thriftique.R

class OrderAdapter(private var orderList: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        // Log all data from the API response for debugging
        println("DEBUG: Order ID=${order.id}, Status=${order.status}, Price=${order.total_price}, Image=${order.image_url}")

        // Set order ID and status
        holder.tvOrderId.text = "Order ID: ${order.id ?: "N/A"}"
        holder.tvStatus.text = "Status: ${order.status ?: "Pending"}"

        // Handle price safely
        val price = order.total_price?.replace("₱", "")?.toDoubleOrNull() ?: 0.0
        holder.tvTotalPrice.text = "Total: ₱$price"

        // Handle image URL (check if the URL is valid and not empty)
        val imageUrl = order.image_url

        if (!imageUrl.isNullOrEmpty()) {
            println("DEBUG: Loading Image URL: $imageUrl")
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.user)  // Default placeholder image
                .error(R.drawable.user)  // Default error image
                .into(holder.ivOrderImage)
        } else {
            println("DEBUG: Image URL is null or empty for Order ID: ${order.id}")
            holder.ivOrderImage.setImageResource(R.drawable.user)  // Default fallback image
        }
    }


    override fun getItemCount(): Int = orderList.size

    // Update order list and notify adapter of the change
    fun updateOrders(newOrders: List<Order>) {
        if (orderList != newOrders) {
            orderList = newOrders
            notifyDataSetChanged()
        }
    }

    // ViewHolder to hold the references to the UI elements
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivOrderImage: ImageView = itemView.findViewById(R.id.orderImage)
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvTotalPrice: TextView = itemView.findViewById(R.id.tvTotalPrice)
    }
}
