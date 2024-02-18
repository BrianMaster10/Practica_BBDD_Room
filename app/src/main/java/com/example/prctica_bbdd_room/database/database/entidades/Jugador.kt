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
    val nombre: String,
    val dorsal: Int,
    val posicion: String,
    val nombreEquiFK: String
)