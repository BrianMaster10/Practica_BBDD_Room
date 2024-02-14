package com.example.prctica_bbdd_room.database.database
// Partido.kt
import java.util.Date

data class Partido(
    val id: Int, // Identificador único del partido
    val equipoLocalId: Int, // Identificador del equipo local
    val equipoVisitanteId: Int, // Identificador del equipo visitante
    val fecha: Date,
    val resultadoLocal: Int,
    val resultadoVisitante: Int
){
    // Constructor secundario si es necesario
    constructor(equipoLocalId: Int, equipoVisitanteId: Int, fecha: Date,
                resultadoLocal: Int, resultadoVisitante: Int) :
            this(0, equipoLocalId, equipoVisitanteId, fecha, resultadoLocal, resultadoVisitante)
}