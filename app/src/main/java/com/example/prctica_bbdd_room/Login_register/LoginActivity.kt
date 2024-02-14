package com.example.prctica_bbdd_room.Login_register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prctica_bbdd_room.databinding.ActivityMainBinding
import com.example.prctica_bbdd_room.actividades.MenuActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)

        // Comprueba si el usuario ha iniciado sesión en los últimos dos días
        val lastLoginTime = sharedPreferences.getLong("lastLoginTime", 0)
        if (System.currentTimeMillis() - lastLoginTime < 2 * 24 * 60 * 60 * 1000) {
            // Han pasado menos de dos días desde el último inicio de sesión
            // Redirige al usuario al menú principal
            redirectToMainMenu()
        }

        binding.loginButton.setOnClickListener {
            val enteredUsername = binding.usernameEditText.text.toString()
            val enteredPassword = binding.passwordEditText.text.toString()
            val rememberUser = binding.checkBoxRecordar.isChecked // Verifica si el usuario quiere ser recordado

            // Comprueba si los campos de entrada están vacíos
            if (enteredUsername.isBlank() || enteredPassword.isBlank()) {
                // Mostrar un mensaje de error si alguno de los campos está vacío
                showError(ErrorType.EMPTY_FIELDS)
                return@setOnClickListener
            }

            // Comprueba si el nombre de usuario y la contraseña son correctos
            if (enteredUsername == sharedPreferences.getString("username", "brian") &&
                enteredPassword == sharedPreferences.getString("password", "brian1")) {
                // El usuario ha iniciado sesión correctamente
                // Guarda la hora del inicio de sesión
                sharedPreferences.edit().putLong("lastLoginTime", System.currentTimeMillis()).apply()

                // Guarda el estado del checkbox para recordar al usuario
                sharedPreferences.edit().putBoolean("rememberUser", rememberUser).apply()

                // Redirige al usuario al menú principal
                redirectToMainMenu()
            } else {
                // El nombre de usuario o la contraseña son incorrectos
                // Muestra un mensaje de error
                showError(ErrorType.INVALID_CREDENTIALS)
            }
        }

        // Restaura el estado del checkbox
        binding.checkBoxRecordar.isChecked = sharedPreferences.getBoolean("rememberUser", false)
    }

    private fun redirectToMainMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish() // Esto evita que el usuario pueda regresar a LoginActivity presionando el botón de retroceso
    }


    // Método para manejar el clic en el enlace de registro
    fun navigateToRegisterActivity(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun showError(errorType: ErrorType) {
        val errorMessage = when (errorType) {
            ErrorType.INVALID_CREDENTIALS -> "Credenciales inválidas. Por favor, verifica tu nombre de usuario y contraseña."
            ErrorType.CONNECTION_ERROR -> "Hubo un problema de conexión. Por favor, inténtalo de nuevo más tarde."
            ErrorType.EMPTY_FIELDS -> "Por favor, completa todos los campos."
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    enum class ErrorType {
        INVALID_CREDENTIALS,
        CONNECTION_ERROR,
        EMPTY_FIELDS
    }
}

