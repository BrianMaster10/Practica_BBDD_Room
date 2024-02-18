package com.example.prctica_bbdd_room.database.database.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "equipos", indices = [Index(value = ["nombreEqui"], unique = true)])
data class Equipo(
    @PrimaryKey(autoGenerate = false)
    val nombreEqui: String,
    val ciudad: String,
    val nombrePabellon: String
): Serializable