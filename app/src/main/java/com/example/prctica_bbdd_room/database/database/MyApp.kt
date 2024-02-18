package com.example.prctica_bbdd_room.database.database
import android.app.Application
import androidx.room.Room
import com.example.prctica_bbdd_room.database.database.MyAppDatabase

class MyApp : Application() {

    companion object {
        lateinit var database: MyAppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        // Inicializar la base de datos aquí
        database = Room.databaseBuilder(
            applicationContext,
            MyAppDatabase::class.java, "myapp-database"
        ).build()
    }
}



