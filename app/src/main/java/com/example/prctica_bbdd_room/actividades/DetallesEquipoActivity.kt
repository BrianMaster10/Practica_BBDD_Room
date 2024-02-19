package com.example.prctica_bbdd_room.actividades
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.adapters.JugadorAdapter
import com.example.prctica_bbdd_room.database.database.MyApp
import com.example.prctica_bbdd_room.database.database.dao.JugadorDao
import com.example.prctica_bbdd_room.database.database.entidades.Equipo
import com.example.prctica_bbdd_room.database.database.entidades.Jugador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetallesEquipoActivity : AppCompatActivity() {

    private lateinit var jugadorAdapter: JugadorAdapter
    private val jugadorDao: JugadorDao by lazy {
        MyApp.database.jugadorDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_equipo)
        val equipo: Equipo? = intent.getSerializableExtra("equipo") as? Equipo
        equipo?.let {
            configurarRecyclerView(it)
        }
        // Configurar el OnClickListener para el botón de agregar jugador
        findViewById<Button>(R.id.insertarButton).setOnClickListener {
            mostrarDialogoInsertarJugador()
        }
        findViewById<Button>(R.id.actualizarButton).setOnClickListener {
            // Obtener el jugador seleccionado para actualizar
            val jugadorSeleccionado = jugadorAdapter.getSelectedJugador()
            if (jugadorSeleccionado != null) {
                mostrarDialogoActualizarJugador(jugadorSeleccionado)
            } else {
                Toast.makeText(this, "Seleccione un jugador para actualizar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarRecyclerView(equipo: Equipo) {
        val jugadoresRecyclerView: RecyclerView = findViewById(R.id.jugadoresRecyclerView)
        jugadorAdapter = JugadorAdapter(mutableListOf())
        jugadoresRecyclerView.layoutManager = LinearLayoutManager(this)
        jugadoresRecyclerView.adapter = jugadorAdapter

        // Obtener la lista de jugadores asociados al equipo
        lifecycleScope.launch(Dispatchers.Main) {
            val jugadores = withContext(Dispatchers.IO) {
                MyApp.database.jugadorDao().getJugadoresByEquipo(equipo.nombreEqui)

            }
            jugadorAdapter.updateItems(jugadores)

        }
        jugadorAdapter.setOnItemClickListener { jugador ->
            mostrarDialogoSeleccionAccion(jugador)
        }
    }

    private fun mostrarDialogoSeleccionAccion(jugador: Jugador) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Qué acción deseas realizar?")
            .setPositiveButton("Actualizar") { dialog, _ ->
                mostrarDialogoActualizarJugador(jugador)
                dialog.dismiss()
            }
            .setNegativeButton("Borrar") { dialog, _ ->
                mostrarDialogoBorrarJugador(jugador)
                dialog.dismiss()
            }
            .setNeutralButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun mostrarDialogoInsertarJugador() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogo_insertar_jugador, null)
        builder.setView(dialogView)
            .setTitle("Insertar Jugador")
            .setPositiveButton("Insertar") { dialog, _ ->
                // Aquí obtienes los datos ingresados por el usuario desde el diálogo
                val nombreJugador = dialogView.findViewById<EditText>(R.id.nombreJugadorEditText).text.toString()
                val dorsalJugador = dialogView.findViewById<EditText>(R.id.dorsalEditText).text.toString().toIntOrNull()
                val posicionJugador = dialogView.findViewById<EditText>(R.id.posicionEditText).text.toString()
                val nombreEquipo = dialogView.findViewById<EditText>(R.id.nombreEquipoEditText).text.toString()

                // Verificar si los campos requeridos no están vacíos
                if (nombreJugador.isNotEmpty() && dorsalJugador != null && posicionJugador.isNotEmpty() && nombreEquipo.isNotEmpty()) {
                    // Crear un nuevo objeto Jugador con los datos ingresados
                    val nuevoJugador = Jugador(nombreJugador, dorsalJugador, posicionJugador, nombreEquipo)

                    // Llamar al método correspondiente en el ViewModel para insertar el jugador en la base de datos
                    lifecycleScope.launch {
                        try {
                            jugadorDao.insertJugador(nuevoJugador)
                            // Agregar el nuevo jugador a la lista local de jugadores
                            jugadorAdapter.agregarJugador(nuevoJugador)
                            // Actualizar la interfaz de usuario
                            jugadorAdapter.notifyItemInserted(jugadorAdapter.itemCount - 1)
                        } catch (e: Exception) {
                            // Manejar cualquier excepción al insertar el jugador en la base de datos
                            Toast.makeText(this@DetallesEquipoActivity, "Error al insertar el jugador", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }

                    dialog.dismiss()
                } else {
                    // Mostrar un mensaje de error si algún campo está vacío
                    Toast.makeText(this@DetallesEquipoActivity, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }


    private fun mostrarDialogoBorrarJugador(jugador: Jugador) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Estás seguro de que quieres borrar este jugador?")
            .setPositiveButton("Sí") { dialog, _ ->
                borrarJugador(jugador)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
    private fun mostrarDialogoActualizarJugador(jugador: Jugador) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogo_actualizar_jugador, null)
        val nombreJugadorEditText = dialogView.findViewById<EditText>(R.id.nombreJugadorEditText)
        val dorsalJugadorEditText = dialogView.findViewById<EditText>(R.id.dorsalJugadorEditText)
        val posicionJugadorEditText = dialogView.findViewById<EditText>(R.id.posicionJugadorEditText)

        // Rellenar los campos del diálogo con los datos actuales del jugador
        nombreJugadorEditText.setText(jugador.nombre)
        dorsalJugadorEditText.setText(jugador.dorsal.toString())
        posicionJugadorEditText.setText(jugador.posicion)

        builder.setView(dialogView)
            .setTitle("Actualizar Jugador")
            .setPositiveButton("Actualizar") { dialog, _ ->
                // Aquí obtienes los datos actualizados ingresados por el usuario desde el diálogo
                val nombreJugadorActualizado = nombreJugadorEditText.text.toString()
                val dorsalJugadorActualizado = dorsalJugadorEditText.text.toString().toIntOrNull()
                val posicionJugadorActualizada = posicionJugadorEditText.text.toString()

                // Verificar si los campos requeridos no están vacíos
                if (nombreJugadorActualizado.isNotEmpty() && dorsalJugadorActualizado != null && posicionJugadorActualizada.isNotEmpty()) {
                    // Actualizar los datos del jugador
                    jugador.apply {
                        nombre = nombreJugadorActualizado
                        dorsal = dorsalJugadorActualizado
                        posicion = posicionJugadorActualizada
                    }

                    // Llamar al método correspondiente en el ViewModel para actualizar el jugador en la base de datos
                    lifecycleScope.launch {
                        try {
                            jugadorDao.updateJugador(jugador)
                            // Notificar al adapter que los datos han cambiado
                            jugadorAdapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            // Manejar cualquier excepción al actualizar el jugador en la base de datos
                            Toast.makeText(this@DetallesEquipoActivity, "Error al actualizar el jugador", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }

                    dialog.dismiss()
                } else {
                    // Mostrar un mensaje de error si algún campo está vacío
                    Toast.makeText(this@DetallesEquipoActivity, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }


    private fun borrarJugador(jugador: Jugador) {
        lifecycleScope.launch {
            // Elimina el jugador de la base de datos
            MyApp.database.jugadorDao().deleteJugador(jugador)

            // Elimina el jugador de la lista en el adapter
            jugadorAdapter.eliminarJugador(jugador)
        }
    }

}

