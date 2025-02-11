package clothing

import ClothingItem
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rendonapp.thriftique.R

class ClothingAdapter(
    private val context: Context,
    private val itemList: List<ClothingItem>,
    onItemClick1: Int,
    private val onItemClick: (ClothingItem) -> Unit // ✅ Click Listener){}
) : RecyclerView.Adapter<ClothingAdapter.ClothingViewHolder>() {

    inner class ClothingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.product_image)
        val titleView: TextView = view.findViewById(R.id.product_title)
        val descriptionView: TextView = view.findViewById(R.id.product_description)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = itemList[position]
                    vibrateDevice() // ✅ Vibrate on click
                    onItemClick(item) // ✅ Trigger click function
                }
            }
        }

        // Function to handle vibration feedback
        private fun vibrateDevice() {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                val effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clothing, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.imageView.setImageResource(currentItem.imageResId)
        holder.titleView.text = currentItem.title
        holder.descriptionView.text = currentItem.description
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
