package com.example.prctica_bbdd_room.database.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.prctica_bbdd_room.database.database.entidades.Clasificacion

@Dao
interface ClasificacionDao {
    @Insert
    suspend fun insertClasificacion(clasificacion: Clasificacion)

    // Obtiene la clasificación de un equipo por su nombre
    @Query("SELECT * FROM clasificaciones WHERE nombreEquiFK = :nombreEquipo")
    suspend fun getClasificacionByEquipo(nombreEquipo: String): Clasificacion
    @Query("UPDATE clasificaciones SET puntos = :puntos WHERE nombreEquiFK = :nombreEquipo")
    suspend fun updatePuntosEquipo(nombreEquipo: String, puntos: Int)
    suspend fun recalcularClasificacion(resultadoDao: ResultadoDao, clasificacionDao: ClasificacionDao) {
        // Obtener todos los resultados de la base de datos
        val resultados = resultadoDao.getResultados()

        // Recorrer los resultados y actualizar los puntos de los equipos
        for (resultado in resultados) {
            val equipoLocal = resultado.equipoLocalFK
            val equipoVisitante = resultado.equipoVisitanteFK
            val golesLocal = resultado.golesLocal
            val golesVisitante = resultado.golesVisitante

            // Calcular los puntos según el resultado del partido
            val puntosEquipoLocal = if (golesLocal > golesVisitante) 1 else 0
            val puntosEquipoVisitante = if (golesVisitante > golesLocal) 1 else 0

            // Actualizar los puntos en la tabla de clasificaciones
            clasificacionDao.updatePuntosEquipo(equipoLocal, puntosEquipoLocal)
            clasificacionDao.updatePuntosEquipo(equipoVisitante, puntosEquipoVisitante)
        }
    }
    //Obtiene una lista con los equipos ordenados en funcion de los puntos
    @Query("SELECT * FROM clasificaciones ORDER BY puntos DESC")
    suspend fun getAllOrderedByPuntosDesc(): List<Clasificacion>

    @Query("UPDATE clasificaciones SET puntos = :puntos WHERE nombreEquiFK = :nombreEquipo")
    suspend fun updateClasamentoByNombreEquipo(nombreEquipo: String, puntos: Int): Int


    // Elimina una clasificación de la base de datos
    @Delete
    suspend fun deleteClasificacion(clasificacion: Clasificacion)
}
