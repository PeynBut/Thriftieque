package Authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.models.ResetPasswordRequest
import com.example.android.models.ResetPasswordResponse
import com.example.android.models.VerifyEmailResponse
import com.rendonapp.thriftique.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass)

        val backBtn: ImageView = findViewById(R.id.backbtn)
        val emailField: TextInputEditText = findViewById(R.id.etEmail)
        val verifyEmailBtn: MaterialButton = findViewById(R.id.verifyEmailBtn)
        val newPasswordLayout: TextInputLayout = findViewById(R.id.newPasswordLayout)
        val confirmPasswordLayout: TextInputLayout = findViewById(R.id.confirmPasswordLayout)
        val newPasswordField: TextInputEditText = findViewById(R.id.etNewPassword)
        val confirmPasswordField: TextInputEditText = findViewById(R.id.etConfirmPassword)
        val resetPasswordBtn: MaterialButton = findViewById(R.id.resetPasswordBtn)

        // Initially hide password fields
        newPasswordLayout.visibility = View.GONE
        confirmPasswordLayout.visibility = View.GONE
        resetPasswordBtn.visibility = View.GONE

        backBtn.setOnClickListener { finish() }

        verifyEmailBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                verifyEmail(email, newPasswordLayout, confirmPasswordLayout, resetPasswordBtn)
            }
        }

        resetPasswordBtn.setOnClickListener {
            val newPassword = newPasswordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please enter both password fields", Toast.LENGTH_SHORT).show()
            } else if (newPassword.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT)
                    .show()
            } else if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(emailField.text.toString(), newPassword)
            }
        }
    }

    private fun verifyEmail(
        email: String,
        newPasswordLayout: TextInputLayout,
        confirmPasswordLayout: TextInputLayout,
        resetPasswordBtn: MaterialButton
    ) {
        RetrofitClient.instance.verifyEmail(email).enqueue(object : Callback<VerifyEmailResponse> {
            override fun onResponse(
                call: Call<VerifyEmailResponse>,
                response: Response<VerifyEmailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        Toast.makeText(
                            this@ForgetPassword,
                            responseBody.message ?: "Email verified! Enter new password.",
                            Toast.LENGTH_LONG
                        ).show()

                        newPasswordLayout.visibility = View.VISIBLE
                        confirmPasswordLayout.visibility = View.VISIBLE
                        resetPasswordBtn.visibility = View.VISIBLE
                    } else {
                        val errorMessage = responseBody?.error ?: "Unexpected error"
                        Toast.makeText(this@ForgetPassword, errorMessage, Toast.LENGTH_SHORT).show()
                        android.util.Log.e("API_ERROR", "Response error: ${responseBody?.error}")
                    }
                } else {
                    val serverError = response.errorBody()?.string() ?: "Server error occurred"
                    Toast.makeText(this@ForgetPassword, serverError, Toast.LENGTH_LONG).show()
                    android.util.Log.e("API_ERROR", "Server error: $serverError")
                }
            }



            override fun onFailure(call: Call<VerifyEmailResponse>, t: Throwable) {
                Toast.makeText(
                    this@ForgetPassword,
                    "Network error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun resetPassword(email: String, newPassword: String) {
        val request = ResetPasswordRequest(email, newPassword)

        RetrofitClient.instance.resetPassword(request)
            .enqueue(object : Callback<ResetPasswordResponse> {
                override fun onResponse(
                    call: Call<ResetPasswordResponse>,
                    response: Response<ResetPasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody?.success != null) { // Check if success has a message
                            Toast.makeText(
                                this@ForgetPassword,
                                responseBody.success, // Display the success message
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            val errorMessage = responseBody?.error ?: "Unexpected error occurred"
                            Toast.makeText(this@ForgetPassword, errorMessage, Toast.LENGTH_SHORT).show()
                            android.util.Log.e("API_ERROR", "Response error: $errorMessage")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        android.util.Log.e("API_ERROR", "Server error: $errorBody")

                        val errorMessage = try {
                            JSONObject(errorBody ?: "").getString("error")
                        } catch (e: Exception) {
                            "Unexpected server error"
                        }

                        Toast.makeText(this@ForgetPassword, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ForgetPassword,
                        "Network error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

}
