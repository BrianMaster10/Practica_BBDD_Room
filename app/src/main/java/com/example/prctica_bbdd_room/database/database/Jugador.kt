package com.example.prctica_bbdd_room.database.database

// Jugador.kt
data class Jugador(
    val id: Int, // Identificador único del jugador
    val nombre: String,
    val dorsal: Int,
    val posicion: String,
    val equipoId: Int // Identificador del equipo al que pertenece el jugador
){
    // Constructor secundario si es necesario
    constructor(nombre: String, dorsal: Int, posicion: String, equipoId: Int) :
            this(0, nombre, dorsal, posicion, equipoId)
}