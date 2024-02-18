package com.example.prctica_bbdd_room.database.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.prctica_bbdd_room.database.database.dao.ClasificacionDao
import com.example.prctica_bbdd_room.database.database.dao.EquipoDao
import com.example.prctica_bbdd_room.database.database.dao.JugadorDao
import com.example.prctica_bbdd_room.database.database.dao.ResultadoDao
import com.example.prctica_bbdd_room.database.database.entidades.Clasificacion
import com.example.prctica_bbdd_room.database.database.entidades.Equipo
import com.example.prctica_bbdd_room.database.database.entidades.Jugador
import com.example.prctica_bbdd_room.database.database.entidades.Resultado

@Database(entities = [Jugador::class, Resultado::class, Equipo::class, Clasificacion::class], version = 2)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
    abstract fun resultadoDao(): ResultadoDao
    abstract fun equipoDao(): EquipoDao
    abstract fun clasificacionDao(): ClasificacionDao

}
