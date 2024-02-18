package com.example.prctica_bbdd_room.actividades

import ClasificacionAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.database.database.MyApp
import com.example.prctica_bbdd_room.database.database.dao.ClasificacionDao
import com.example.prctica_bbdd_room.database.database.dao.ResultadoDao
import com.example.prctica_bbdd_room.database.database.entidades.Clasificacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ClasificacionActivity : AppCompatActivity() {
    private lateinit var clasificacionDao: ClasificacionDao
    private lateinit var resultadoDao: ResultadoDao
    private lateinit var clasificacionRepository: ClasificacionRepository
    private lateinit var clasificacionAdapter: ClasificacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clasificacion)

        clasificacionDao = MyApp.database.clasificacionDao()
        resultadoDao = MyApp.database.resultadoDao()
        clasificacionRepository = ClasificacionRepository(clasificacionDao, resultadoDao)

        val recyclerViewClasificacion = findViewById<RecyclerView>(R.id.recyclerViewClasificacion)
        recyclerViewClasificacion.layoutManager = LinearLayoutManager(this)
        clasificacionAdapter = ClasificacionAdapter(mutableListOf())
        recyclerViewClasificacion.adapter = clasificacionAdapter

        recalcularClasificacion()
    }

    private fun recalcularClasificacion() {
        lifecycleScope.launch(Dispatchers.IO) {
            clasificacionRepository.recalcularClasificacion()

            val clasificaciones = clasificacionRepository.getAllClasificacionesOrderedByPuntosDesc()
            runOnUiThread {
                mostrarClasificacion(clasificaciones)
            }
        }
    }

    private fun mostrarClasificacion(clasificaciones: List<Clasificacion>) {
        clasificacionAdapter.actualizarClasificaciones(clasificaciones)
        clasificacionAdapter.notifyDataSetChanged()
    }
}
