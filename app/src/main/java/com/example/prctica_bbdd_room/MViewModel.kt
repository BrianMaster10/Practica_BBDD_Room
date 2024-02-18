package com.example.prctica_bbdd_room

import androidx.lifecycle.ViewModel
import com.example.prctica_bbdd_room.database.database.dao.JugadorDao
import com.example.prctica_bbdd_room.database.database.entidades.Jugador

class MiViewModel(private val jugadorDao: JugadorDao) : ViewModel() {

    suspend fun insertarJugador(jugador: Jugador) {
        jugadorDao.insertJugador(jugador)
    }

    suspend fun eliminarJugador(jugador: Jugador) {
        jugadorDao.deleteJugador(jugador)
    }

    suspend fun actualizarJugador(jugador: Jugador) {
        jugadorDao.updateJugador(jugador)
    }

    suspend fun getJugadoresByEquipo(nombreEquipo: String): List<Jugador> {
        return jugadorDao.getJugadoresByEquipo(nombreEquipo)
    }
}

