package Category

import Products.ProductAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.models.Product
import com.google.android.material.button.MaterialButton
import com.rendonapp.thriftique.R

class CategoryActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerViewCategorizedProduct: RecyclerView
    private var productList: List<Product> = listOf() // Assume this is populated from a database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Initialize RecyclerView
        recyclerViewCategorizedProduct = findViewById(R.id.recyclerViewCategorizedProduct)
        setupRecyclerView()

        // Set up category buttons
        setupCategoryButtons()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(this, productList) // Ensure adapter is correctly initialized
        recyclerViewCategorizedProduct.layoutManager = LinearLayoutManager(this)
        recyclerViewCategorizedProduct.adapter = productAdapter
    }

    private fun setupCategoryButtons() {
        val categoryButtons = mapOf(
            R.id.buttonAll to "All",
            R.id.buttonOldSchool to "Old School",
            R.id.buttonStreetWear to "Street Wear",
            R.id.buttonCasualFit to "Casual Fit"
        )

        categoryButtons.forEach { (buttonId, category) ->
            findViewById<MaterialButton>(buttonId).setOnClickListener {
                filterProductsByCategory(category)
                Toast.makeText(this, "$category Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterProductsByCategory(category: String) {
        val filteredList = if (category == "All") {
            productList // Show all products
        } else {
            productList.filter { it.category == category }
        }

        productAdapter.updateList(filteredList)
    }
}
