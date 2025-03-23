

import Order.Order
import Products.ProductAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rendonapp.thriftique.R

class OrderAdapter(private var orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val recyclerViewProducts: RecyclerView = itemView.findViewById(R.id.recyclerViewProducts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.tvOrderId.text = order.orderId
        holder.tvStatus.text = order.status

        // ✅ Ensure `order.products` is not null
        val productList = order.products ?: emptyList()

        // ✅ Setup RecyclerView for products
        holder.recyclerViewProducts.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)

        if (productList.isNotEmpty()) {
            // ✅ Pass the `context` properly
            holder.recyclerViewProducts.adapter = ProductAdapter(holder.itemView.context, productList)
        }
    }




    override fun getItemCount(): Int {
        return orders.size
    }

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
