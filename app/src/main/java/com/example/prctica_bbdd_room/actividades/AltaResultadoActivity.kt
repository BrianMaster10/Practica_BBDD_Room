package com.example.prctica_bbdd_room.actividades

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.database.database.MyApp
import com.example.prctica_bbdd_room.database.database.dao.ResultadoDao
import com.example.prctica_bbdd_room.database.database.entidades.Resultado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AltaResultadoActivity : AppCompatActivity() {
    private lateinit var resultadoDao: ResultadoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_resultado)

        // Inicializar el DAO de resultados
        resultadoDao = MyApp.database.resultadoDao()

        val buttonGuardar = findViewById<Button>(R.id.buttonGuardar)
        buttonGuardar.setOnClickListener {
            guardarResultado()

        }

    }

    private fun guardarResultado() {
        // Obtener datos ingresados por el usuario desde las vistas del XML
        val editTextEquipoLocal = findViewById<EditText>(R.id.editTextEquipoLocal)
        val editTextEquipoVisitante = findViewById<EditText>(R.id.editTextEquipoVisitante)
        val editTextGolesLocal = findViewById<EditText>(R.id.editTextGolesLocal)
        val editTextGolesVisitante = findViewById<EditText>(R.id.editTextGolesVisitante)
        val editTextFecha = findViewById<EditText>(R.id.editTextFecha)

        val nombreEquipoLocal = editTextEquipoLocal.text.toString()
        val nombreEquipoVisitante = editTextEquipoVisitante.text.toString()

        val golesLocalStr = editTextGolesLocal.text.toString()
        val golesLocal = if (golesLocalStr.isNotEmpty()) golesLocalStr.toInt() else 0

        val golesVisitanteStr = editTextGolesVisitante.text.toString()
        val golesVisitante = if (golesVisitanteStr.isNotEmpty()) golesVisitanteStr.toInt() else 0

        val fechaPartido = editTextFecha.text.toString()

        // Crear un nuevo objeto Resultado con los datos ingresados por el usuario
        val nuevoResultado = Resultado(
            0, // No es necesario establecer el ID ya que se genera automáticamente
            nombreEquipoLocal,
            nombreEquipoVisitante,
            golesLocal,
            golesVisitante,
            fechaPartido
        )

        // Después de insertar el nuevo resultado en la base de datos en un hilo de fondo
        lifecycleScope.launch(Dispatchers.IO) {
            resultadoDao.insertResultado(nuevoResultado)
            // Establecer el resultado como OK
            setResult(RESULT_OK)
            // Finalizar la actividad
            finish()
        }

    }


}
