package com.example.prctica_bbdd_room.database.database.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "resultados",
    foreignKeys = [ForeignKey(
        entity = Equipo::class,
        parentColumns = ["nombreEqui"],
        childColumns = ["equipoLocalFK"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Equipo::class,
        parentColumns = ["nombreEqui"],
        childColumns = ["equipoVisitanteFK"],
        onDelete = ForeignKey.CASCADE
    )],indices = [Index("equipoLocalFK"), Index("equipoVisitanteFK")])
data class Resultado(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val equipoLocalFK: String,
    val equipoVisitanteFK: String,
    val golesLocal: Int,
    val golesVisitante: Int,
    val dia: String
)
