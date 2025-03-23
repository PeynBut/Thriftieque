package Category

import Products.ProductAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.models.ApiResponse
import com.example.android.models.Product
import com.google.android.material.button.MaterialButton
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerViewCategorizedProduct: RecyclerView
    private var productList: List<Product> = listOf() // Assume this is populated from a database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // ✅ Set up the back button
        setupBackButton()

        // ✅ Initialize RecyclerView
        recyclerViewCategorizedProduct = findViewById(R.id.recyclerViewCategorizedProduct)

        getProductsFromDatabaseOrAPI() // ✅ Correct! UI updates automatically

        setupRecyclerView()
        setupCategoryButtons()
    }



    private fun getProductsFromDatabaseOrAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getProducts().execute() // Synchronous Call
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    withContext(Dispatchers.Main) { // Switch to Main Thread to update UI
                        if (apiResponse?.status == "success") {
                            productList = apiResponse.products ?: emptyList() // Ensure non-null
                            productAdapter.updateList(productList)
                        } else {
                            Toast.makeText(
                                this@CategoryActivity,
                                "No products found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CategoryActivity, "Response error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CategoryActivity,
                        "Failed to load products",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }




    private fun setupBackButton() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.categoryToolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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
