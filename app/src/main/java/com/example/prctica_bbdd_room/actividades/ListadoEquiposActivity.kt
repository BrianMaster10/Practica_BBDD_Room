package com.example.prctica_bbdd_room.actividades

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.adapters.EquipoAdapter
import com.example.prctica_bbdd_room.database.database.MyApp
import com.example.prctica_bbdd_room.database.database.entidades.Equipo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListadoEquiposActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextBuscar: EditText
    private lateinit var equipoAdapter: EquipoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_equipos)

        recyclerView = findViewById(R.id.recyclerViewEquipos)
        editTextBuscar = findViewById(R.id.editTextBuscar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        equipoAdapter = EquipoAdapter(emptyList()) { equipo ->
            // Manejar la selección del equipo aquí
            // Por ejemplo, iniciar una nueva actividad para mostrar detalles o gestionar jugadores
            val intent = Intent(this, DetallesEquipoActivity::class.java)
            intent.putExtra("equipo", equipo)
            startActivity(intent)
        }
        recyclerView.adapter = equipoAdapter

        // Agregar el TextWatcher después de asignar la referencia al EditText
        editTextBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nombreEquipo = s.toString()
                buscarEquiposPorNombre(nombreEquipo)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        cargarEquipos()
    }


    private fun cargarEquipos() {
        lifecycleScope.launch(Dispatchers.Main) {
            val equipos = withContext(Dispatchers.IO) {
                MyApp.database.equipoDao().getEquipos()
            }
            equipoAdapter.setItems(equipos)
        }
    }

    private fun buscarEquiposPorNombre(nombre: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            val equipos = withContext(Dispatchers.IO) {
                MyApp.database.equipoDao().getEquiposByNombre(nombre)
            }
            equipoAdapter.setItems(equipos)
        }
    }

}
//R.id.menu_detalles_equipo -> {
//                navigateToDetallesEquipoActivity()
//                return true
//            }
private fun Intent.putExtra(key: String, equipo: Equipo) {

}
