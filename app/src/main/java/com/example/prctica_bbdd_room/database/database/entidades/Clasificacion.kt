package com.example.prctica_bbdd_room.database.database.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "clasificaciones",
    foreignKeys = [ForeignKey(
        entity = Equipo::class,
        parentColumns = ["nombreEqui"],
        childColumns = ["nombreEquiFK"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("nombreEquiFK")])
data class Clasificacion(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val puntos: Int,
    val nombreEquiFK: String
)