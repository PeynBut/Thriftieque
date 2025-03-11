package Authentication

import ApiService
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.HapticFeedbackConstants
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.models.ApiResponse
import com.example.android.models.RegisterUserRequest
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.rendonapp.thriftique.Homepage
import com.rendonapp.thriftique.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPart2 : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var etPhone: TextInputEditText
    private lateinit var etBarangay: TextInputEditText
    private lateinit var etMunicipality: TextInputEditText
    private lateinit var etProvince: TextInputEditText
    private lateinit var etCountry: TextInputEditText
    private lateinit var etPostalCode: TextInputEditText
    private lateinit var registerButton: MaterialButton
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_part2) // ✅ Ensure this matches the correct XML file

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Initialize views AFTER setting the content view
        backBtn = findViewById(R.id.backbtn)
        etPhone = findViewById(R.id.et_phone)
        etBarangay = findViewById(R.id.et_barangay)
        etMunicipality = findViewById(R.id.et_municipality)
        etProvince = findViewById(R.id.et_province)
        etCountry = findViewById(R.id.et_country)
        etPostalCode = findViewById(R.id.et_postal_code)
        registerButton = findViewById(R.id.registerButton)

        // Set up click listener for back button
        backBtn.setOnClickListener {
            finish() // Go back to the previous activity
        }

        registerButton.setOnClickListener { view ->
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            vibrate()

            if (validateInputs()) {
                val phone = etPhone.text.toString().trim()
                val barangay = etBarangay.text.toString().trim()
                val municipality = etMunicipality.text.toString().trim()
                val country = etCountry.text.toString().trim()
                val province = etProvince.text.toString().trim()
                val postalCode = etPostalCode.text.toString().trim()

                registerUser(phone, barangay, municipality, country, province, postalCode)
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (etPhone.text.toString().trim().isEmpty()) {
            etPhone.error = "Enter phone number"
            isValid = false
        }
        if (etBarangay.text.toString().trim().isEmpty()) {
            etBarangay.error = "Enter barangay"
            isValid = false
        }
        if (etMunicipality.text.toString().trim().isEmpty()) {
            etMunicipality.error = "Enter municipality"
            isValid = false
        }
        if (etCountry.text.toString().trim().isEmpty()) {
            etCountry.error = "Enter country"
            isValid = false
        }
        if (etProvince.text.toString().trim().isEmpty()) {
            etProvince.error = "Enter province"
            isValid = false
        }
        if (etPostalCode.text.toString().trim().isEmpty()) {
            etPostalCode.error = "Enter postal code"
            isValid = false
        }
        return isValid
    }

    private fun registerUser(phone: String, barangay: String, municipality: String, country: String, province: String, postalCode: String) {
        val progressDialog = AlertDialog.Builder(this)
            .setTitle("Registering")
            .setMessage("Please wait...")
            .setCancelable(false)
            .create()

        progressDialog.show()

        val userDetails = RegisterUserRequest(phone, barangay, municipality, country, province, postalCode)

        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)

        apiService.registerUserPart2(userDetails).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                progressDialog.dismiss()
                Log.d("API Response", "Response Code: ${response.code()}, Body: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@RegisterPart2, "Registration Complete", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterPart2, Homepage::class.java))
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(this@RegisterPart2, "Registration failed: $errorMessage", Toast.LENGTH_LONG).show()
                    Log.e("API Error", errorMessage)
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@RegisterPart2, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to handle vibration feedback
    private fun vibrate() {
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}