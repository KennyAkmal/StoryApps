package com.submission.storyapps.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.submission.storyapps.databinding.ActivityRegisterBinding
import com.submission.storyapps.model.RegisterRequest
import com.submission.storyapps.model.RegisterResponse
import com.submission.storyapps.network.ApiClient
import com.submission.storyapps.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (validateInput(name, email, password)) {
                userRegister(this@RegisterActivity, name, email, password)
            }
        }
    }
    private fun validateInput(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                showToast("Nama tidak boleh kosong")
                false
            }
            email.isEmpty() -> {
                showToast("Email tidak boleh kosong")
                false
            }
            password.isEmpty() -> {
                showToast("Password tidak boleh kosong")
                false
            }
            password.length < 8 -> {
                showToast("Password minimal 8 karakter")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }
    private fun userRegister(context: Context, name: String, email: String, password: String) {
        val request = RegisterRequest(name, email, password)
        val apiService = ApiClient.create(context)

        apiService.userRegister(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Toast.makeText(this@RegisterActivity, "Registrasi berhasil: ${it.message}", Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Registrasi gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}