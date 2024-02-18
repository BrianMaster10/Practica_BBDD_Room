package com.example.prctica_bbdd_room.actividades

import ClasificacionAdapter
import android.os.Bundle
import android.widget.Toast
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
    private lateinit var clasificacionAdapter: ClasificacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clasificacion)

        // Inicializar el DAO de clasificaciones
        clasificacionDao = MyApp.database.clasificacionDao()
        // Inicializar el DAO de resultados
        resultadoDao = MyApp.database.resultadoDao()

        // Configurar el RecyclerView
        val recyclerViewClasificacion = findViewById<RecyclerView>(R.id.recyclerViewClasificacion)
        recyclerViewClasificacion.layoutManager = LinearLayoutManager(this)
        clasificacionAdapter = ClasificacionAdapter(mutableListOf())
        recyclerViewClasificacion.adapter = clasificacionAdapter

        // Calcular la clasificación y mostrarla en la interfaz de usuario
        recalcularClasificacion()
    }

    private fun mostrarClasificacion(clasificaciones: List<Clasificacion>) {
        // Mostrar el tamaño de la lista en un Toast
        val sizeMessage = "Tamaño de la lista de clasificaciones: ${clasificaciones.size}"
        Toast.makeText(this@ClasificacionActivity, sizeMessage, Toast.LENGTH_SHORT).show()

        // Actualizar los datos del adaptador con la nueva lista de clasificaciones
        clasificacionAdapter.actualizarClasificaciones(clasificaciones)
        // Notificar al RecyclerView de los cambios en los datos
        clasificacionAdapter.notifyDataSetChanged()
    }

    private fun recalcularClasificacion() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Obtener la lista de resultados desde la base de datos
            val resultados = resultadoDao.getResultados()

            // Recorrer la lista de resultados y recalcular la clasificación para cada equipo involucrado
            for (resultado in resultados) {
                val equipoLocal = resultado.equipoLocalFK
                val equipoVisitante = resultado.equipoVisitanteFK
                val golesLocal = resultado.golesLocal
                val golesVisitante = resultado.golesVisitante

                // Calcular los puntos para el equipo local y visitante según los goles
                var puntosEquipoLocal = 0
                var puntosEquipoVisitante = 0
                if (golesLocal > golesVisitante) {
                    puntosEquipoLocal = 1
                } else if (golesLocal < golesVisitante) {
                    puntosEquipoVisitante = 1
                }

                // Insertar los datos de clasificación si no existen en la tabla
                // Esto evita problemas cuando no hay datos de clasificación para un equipo
                if (clasificacionDao.getClasificacionByEquipo(equipoLocal) == null) {
                    clasificacionDao.insertClasificacion(Clasificacion(0, 0, equipoLocal))
                }
                if (clasificacionDao.getClasificacionByEquipo(equipoVisitante) == null) {
                    clasificacionDao.insertClasificacion(Clasificacion(0, 0, equipoVisitante))
                }

                // Actualizar la clasificación en la base de datos para cada equipo
                clasificacionDao.updateClasamentoByNombreEquipo(equipoLocal, puntosEquipoLocal)
                clasificacionDao.updateClasamentoByNombreEquipo(equipoVisitante, puntosEquipoVisitante)
            }

            // Obtener la lista actualizada de clasificaciones ordenadas por puntos
            val clasificaciones = clasificacionDao.getAllOrderedByPuntosDesc()

            // Después de actualizar todas las clasificaciones, volver a mostrar la clasificación en la interfaz de usuario
            runOnUiThread {
                mostrarClasificacion(clasificaciones)
            }
        }
    }
}
