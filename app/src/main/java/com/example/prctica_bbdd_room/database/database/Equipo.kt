package com.example.prctica_bbdd_room.database.database

data class Equipo(
    val id: Int, // Identificador único del equipo
    val nombre: String,
    val ciudad: String,
    val nombrePabellon: String
) {
    // Constructor secundario si es necesario
    constructor(nombre: String, ciudad: String, nombrePabellon: String) :
            this(0, nombre, ciudad, nombrePabellon)
}