package com.example.prctica_bbdd_room.database.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prctica_bbdd_room.database.database.Partido

@Dao
interface PartidoDao {
    @Query("SELECT * FROM Partido")
    fun getAll(): List<Partido>

    @Insert
    fun insertAll(vararg partidos: Partido)
}