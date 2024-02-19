package com.example.prctica_bbdd_room.database.database.entidades


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "jugadores",
    foreignKeys = [ForeignKey(
        entity = Equipo::class,
        parentColumns = ["nombreEqui"],
        childColumns = ["nombreEquiFK"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("nombreEquiFK")])
data class Jugador(
    @PrimaryKey
    var nombre: String,
    var dorsal: Int,
    var posicion: String,
    val nombreEquiFK: String

)