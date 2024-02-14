package com.example.prctica_bbdd_room.Login_register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.prctica_bbdd_room.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)

        binding.registerButton.setOnClickListener {
            val enteredUsername = binding.usernameEditText.text.toString()
            val enteredPassword = binding.passwordEditText.text.toString()

            // Guardar las credenciales en SharedPreferences
            sharedPreferences.edit().apply {
                putString("username", enteredUsername)
                putString("password", enteredPassword)
                putLong("lastLoginTime", System.currentTimeMillis())
            }.apply()

            // Redirigir al usuario de nuevo a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
