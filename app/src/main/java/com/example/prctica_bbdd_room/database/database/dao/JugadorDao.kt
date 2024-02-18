package com.example.prctica_bbdd_room.database.database.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.prctica_bbdd_room.database.database.entidades.Jugador

@Dao
interface JugadorDao {
    @Insert
    suspend fun insertJugador(jugador: Jugador)
    @Query("SELECT * FROM jugadores WHERE nombreEquiFK = :nombreEquipo")
    suspend fun getJugadoresByEquipo(nombreEquipo: String): List<Jugador>

    // Elimina un jugador de la base de datos
    @Delete
    suspend fun deleteJugador(jugador: Jugador)

    @Update
    suspend fun updateJugador(jugador: Jugador)
    // Agrega otros métodos de consulta y actualización según sea necesario
}