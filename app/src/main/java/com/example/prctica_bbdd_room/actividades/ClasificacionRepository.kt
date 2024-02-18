package com.example.prctica_bbdd_room.actividades

import com.example.prctica_bbdd_room.database.database.dao.ClasificacionDao
import com.example.prctica_bbdd_room.database.database.dao.ResultadoDao
import com.example.prctica_bbdd_room.database.database.entidades.Clasificacion

class ClasificacionRepository(private val clasificacionDao: ClasificacionDao, private val resultadoDao: ResultadoDao) {

    suspend fun recalcularClasificacion() {
        val resultados = resultadoDao.getResultados()
        for (resultado in resultados) {
            val equipoLocal = resultado.equipoLocalFK
            val equipoVisitante = resultado.equipoVisitanteFK
            val golesLocal = resultado.golesLocal
            val golesVisitante = resultado.golesVisitante

            // Determinar el resultado del partido
            val resultadoPartido = when {
                golesLocal > golesVisitante -> ResultadoPartido.GANADO
                golesLocal < golesVisitante -> ResultadoPartido.PERDIDO
                else -> ResultadoPartido.EMPATADO
            }

            // Obtener los puntos actuales de cada equipo
            val clasificacionLocal = clasificacionDao.getClasificacionByEquipo(equipoLocal)
            val clasificacionVisitante = clasificacionDao.getClasificacionByEquipo(equipoVisitante)

            // Verificar si se encontraron las clasificaciones para los equipos
            if (clasificacionLocal != null && clasificacionVisitante != null) {
                // Sumar o restar puntos según el resultado del partido
                var puntosAnterioresLocal = clasificacionLocal.puntos
                var puntosAnterioresVisitante = clasificacionVisitante.puntos

                // Actualizar los puntos según el resultado del partido
                when (resultadoPartido) {
                    ResultadoPartido.GANADO -> {
                        val nuevosPuntosLocal = puntosAnterioresLocal + 1
                        val nuevosPuntosVisitante = puntosAnterioresVisitante
                        clasificacionDao.updatePuntosEquipo(equipoLocal, nuevosPuntosLocal)
                        clasificacionDao.updatePuntosEquipo(equipoVisitante, nuevosPuntosVisitante)
                    }
                    ResultadoPartido.EMPATADO -> {
                        val nuevosPuntosLocal = puntosAnterioresLocal
                        val nuevosPuntosVisitante = puntosAnterioresVisitante
                        clasificacionDao.updatePuntosEquipo(equipoLocal, nuevosPuntosLocal)
                        clasificacionDao.updatePuntosEquipo(equipoVisitante, nuevosPuntosVisitante)
                    }
                    ResultadoPartido.PERDIDO -> {
                        val nuevosPuntosLocal = puntosAnterioresLocal
                        val nuevosPuntosVisitante = puntosAnterioresVisitante + 1
                        clasificacionDao.updatePuntosEquipo(equipoLocal, nuevosPuntosLocal)
                        clasificacionDao.updatePuntosEquipo(equipoVisitante, nuevosPuntosVisitante)
                    }
                }
            } else {
                // Manejar el caso cuando no se encuentran las clasificaciones de los equipos
            }
        }
    }

    suspend fun getAllClasificacionesOrderedByPuntosDesc(): List<Clasificacion> {
        return clasificacionDao.getAllOrderedByPuntosDesc()
    }

    enum class ResultadoPartido {
        GANADO,
        EMPATADO,
        PERDIDO
    }
}

