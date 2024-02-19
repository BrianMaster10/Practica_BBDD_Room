package com.example.prctica_bbdd_room.actividades

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prctica_bbdd_room.adapters.EquipoAdapter
import com.example.prctica_bbdd_room.adapters.JugadorAdapter
import com.example.prctica_bbdd_room.database.database.MyApp
import com.example.prctica_bbdd_room.database.database.entidades.Equipo
import com.example.prctica_bbdd_room.database.database.entidades.Jugador
import com.example.prctica_bbdd_room.databinding.ActivityAltaEquipoBinding
import kotlinx.coroutines.launch

class AltaEquipoActivity : AppCompatActivity() {

    private lateinit var adapter: JugadorAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: ActivityAltaEquipoBinding
    private lateinit var equipo: Equipo
    private val jugadoresList = mutableListOf<Jugador>()
    private lateinit var nombreJugadorEditText: EditText
    private lateinit var dorsalEditText: EditText
    private lateinit var posicionEditText: EditText
    private lateinit var equipoAdapter: EquipoAdapter


    private lateinit var jugadorAdapter: JugadorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar la base de datos aquí
        MyApp.database

        binding = ActivityAltaEquipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el RecyclerView
        jugadorAdapter = JugadorAdapter(mutableListOf()) // Inicializar el adapter con una lista vacía
        binding.verJugadoresButton.setOnClickListener {
            val nombreEquipo = binding.nombreEquipoTextView.text.toString()
            lifecycleScope.launch {
                try {
                    // Obtener la lista de equipos
                    val equipos = MyApp.database.equipoDao().getEquipos()
                    if (equipos.isNotEmpty()) {
                        startActivity<ListadoEquiposActivity>()
                    } else {
                        // Si no hay equipos, mostrar un mensaje indicando que no hay equipos creados
                        Toast.makeText(this@AltaEquipoActivity, "No hay equipos creados", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Manejar cualquier excepción al cargar los equipos
                    Toast.makeText(this@AltaEquipoActivity, "Error al cargar los equipos", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }

        // Configurar el OnClickListener para el botón de agregar jugador
        binding.agregarJugadorButton.setOnClickListener {
            agregarJugador()
        }

        // Configurar el OnClickListener para el botón de guardar equipo
        binding.guardarEquipoButton.setOnClickListener {
            guardarEquipo()
        }
    }
    private fun cargarYMostrarJugadores() {
        val nombreEquipo = binding.nombreEquipoTextView.text.toString()
        lifecycleScope.launch {
            try {
                // Obtener la lista de jugadores asociados al equipo actual
                val jugadores = MyApp.database.jugadorDao().getJugadoresByEquipo(nombreEquipo)
                // Actualizar el adaptador del RecyclerView con la lista de jugadores
                jugadorAdapter.updateItems(jugadores)
            } catch (e: Exception) {
                // Manejar cualquier excepción al cargar los jugadores
                Toast.makeText(this@AltaEquipoActivity, "Error al cargar los jugadores", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
    // Define una función de extensión en el nivel superior que te permitirá iniciar cualquier actividad desde cualquier lugar
    inline fun <reified T : Any> Context.startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }
    private fun agregarJugador() {
        // Obtener referencias a los elementos de la interfaz de usuario utilizando el binding
        val nombreJugadorEditText = binding.nombreJugadorEditText
        val dorsalEditText = binding.dorsalEditText
        val posicionEditText = binding.posicionEditText

        // Obtener el texto de los campos de texto
        val nombreJugador = nombreJugadorEditText.text.toString()
        val dorsal = dorsalEditText.text.toString().toIntOrNull()
        val posicion = posicionEditText.text.toString()
        val nombreEquipo = binding.nombreEquipoTextView.text.toString()

        // Verificar si los campos requeridos no están vacíos
        if (nombreJugador.isNotEmpty() && dorsal != null && posicion.isNotEmpty() && nombreEquipo.isNotEmpty()) {
            // Verificar si existen equipos creados en la base de datos
            lifecycleScope.launch {
                val equipos = MyApp.database.equipoDao().getEquipos()
                if (equipos.isNotEmpty()) {
                    // Si existen equipos, crear el jugador y guardarlo en la base de datos
                    val jugador = Jugador(nombreJugador, dorsal, posicion, nombreEquipo)
                    try {
                        MyApp.database.jugadorDao().insertJugador(jugador)

                        // Añadir el jugador a la lista local para que aparezca en el RecyclerView
                        jugadoresList.add(jugador)
                        jugadorAdapter.notifyDataSetChanged() // Notificar al adaptador que se ha actualizado la lista
                        finish()
                    } catch (e: SQLiteConstraintException) {
                        // Manejar la excepción si hay una violación de la clave única
                        Toast.makeText(this@AltaEquipoActivity, "Error: El jugador ya existe", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // Manejar cualquier otra excepción
                        Toast.makeText(this@AltaEquipoActivity, "Error al agregar el jugador", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                } else {
                    // Si no hay equipos creados en la base de datos, mostrar un mensaje
                    Toast.makeText(this@AltaEquipoActivity, "Error: No hay equipos creados", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Mostrar un mensaje de error si algún campo está vacío
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }


    private fun guardarEquipo() {
        val nombreEquipo = binding.nombreEquipoTextView.text.toString()
        val ciudad = binding.ciudadTextView.text.toString()
        val pabellon = binding.pabellonTextView.text.toString()

        val equipo = Equipo(nombreEquipo, ciudad, pabellon)

        lifecycleScope.launch {
            try {
                // Insertar el equipo en la base de datos
                MyApp.database.equipoDao().insertEquipo(equipo)

                // Insertar jugadores asociados al equipo
                for (jugador in jugadoresList) {
                    MyApp.database.jugadorDao().insertJugador(jugador)
                }

                finish() // Finalizar la actividad después de guardar el equipo
            } catch (e: Exception) {
                // Manejar cualquier excepción al guardar el equipo
                Toast.makeText(this@AltaEquipoActivity, "Error al guardar el equipo", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}


