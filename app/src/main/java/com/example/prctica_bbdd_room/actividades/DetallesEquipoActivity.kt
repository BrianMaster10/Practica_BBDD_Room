package com.example.prctica_bbdd_room.actividades

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prctica_bbdd_room.MiViewModel
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.adapters.JugadorAdapter
import com.example.prctica_bbdd_room.database.database.MyApp
import com.example.prctica_bbdd_room.database.database.entidades.Equipo
import com.example.prctica_bbdd_room.database.database.entidades.Jugador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetallesEquipoActivity : AppCompatActivity() {

    private lateinit var viewModel: MiViewModel
    private lateinit var jugadorAdapter: JugadorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_equipo)

        val equipo: Equipo? = intent.getSerializableExtra("equipo") as? Equipo

        if (equipo != null) {
            val nombreTextView: TextView = findViewById(R.id.nombreEquipoTextView)
            val ciudadTextView: TextView = findViewById(R.id.ciudadTextView)
            val pabellonTextView: TextView = findViewById(R.id.pabellonTextView)

            nombreTextView.text = equipo.nombreEqui
            ciudadTextView.text = equipo.ciudad
            pabellonTextView.text = equipo.nombrePabellon

            val jugadoresRecyclerView: RecyclerView = findViewById(R.id.jugadoresRecyclerView)
            jugadorAdapter = JugadorAdapter(mutableListOf())
            jugadoresRecyclerView.layoutManager = LinearLayoutManager(this)
            jugadoresRecyclerView.adapter = jugadorAdapter

            viewModel = MiViewModel(MyApp.database.jugadorDao())

            cargarYMostrarJugadores(equipo)

            val insertarButton: Button = findViewById(R.id.insertarButton)
            insertarButton.setOnClickListener {
                // Abre un diálogo o actividad para ingresar los datos del nuevo jugador
                // Luego, llama al método correspondiente en el ViewModel para insertar el jugador
            }

            val borrarButton: Button = findViewById(R.id.borrarButton)
            borrarButton.setOnClickListener {
                // Aquí puedes implementar la lógica para borrar un jugador seleccionado
                // Por ejemplo:
                // val jugadorABorrar = /* Obtener el jugador seleccionado de alguna manera */
                // viewModel.eliminarJugador(jugadorABorrar)
            }

            val actualizarButton: Button = findViewById(R.id.actualizarButton)
            actualizarButton.setOnClickListener {
                // Abre un diálogo o actividad para modificar los datos del jugador seleccionado
                // Luego, llama al método correspondiente en el ViewModel para actualizar los datos del jugador
            }
        } else {
            Toast.makeText(this, "Error: No se pudo obtener el equipo seleccionado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun cargarYMostrarJugadores(equipo: Equipo) {
        lifecycleScope.launch(Dispatchers.Main) {
            val jugadores = withContext(Dispatchers.IO) {
                viewModel.getJugadoresByEquipo(equipo.nombreEqui)
            }
            jugadorAdapter.updateItems(jugadores)
        }
    }
    private fun mostrarDialogoInsertarJugador() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogo_insertar_jugador, null)
        builder.setView(dialogView)
            .setTitle("Insertar Jugador")
            .setPositiveButton("Insertar") { dialog, _ ->
                // Aquí obtienes los datos ingresados por el usuario desde el diálogo
                val nombreJugador = dialogView.findViewById<EditText>(R.id.nombreJugadorTextView).text.toString()
                val dorsalJugador = dialogView.findViewById<EditText>(R.id.dorsalJugadorEditText).text.toString().toInt()
                val posicionJugador = dialogView.findViewById<EditText>(R.id.posicionJugadorEditText).text.toString()
                val nombreEquipo = dialogView.findViewById<EditText>(R.id.nombreEquipoEditText).text.toString()

                // Luego, creas un nuevo objeto Jugador con los datos ingresados
                val nuevoJugador = Jugador(nombreJugador, dorsalJugador, posicionJugador, nombreEquipo)


                // Llamas al método correspondiente en el ViewModel para insertar el jugador
                lifecycleScope.launch {
                    viewModel.insertarJugador(nuevoJugador)
                }
                // Actualizas la lista en el adapter después de insertar el jugador
                jugadorAdapter.agregarJugador(nuevoJugador)

                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

}




