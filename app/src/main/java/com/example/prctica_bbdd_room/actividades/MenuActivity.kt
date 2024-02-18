package com.example.prctica_bbdd_room.actividades

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.prctica_bbdd_room.R

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_listado_equipos -> {
                navigateToListadoEquiposActivity()
                return true
            }
            R.id.menu_clasificacion -> {
                navigateToClasificacionActivity()
                return true
            }
            R.id.menu_AltaResultado -> {
                navigateToAltaResultadoActivity()
                return true
            }
            R.id.menu_AltaEquipo -> {
                navigateToAltaEquipoActivity()
                return true
            }
            // Agrega más opciones de menú según sea necesario
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToListadoEquiposActivity() {
        val intent = Intent(this, ListadoEquiposActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToDetallesEquipoActivity() {
        val intent = Intent(this, DetallesEquipoActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToClasificacionActivity() {
        val intent = Intent(this, ClasificacionActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToAltaResultadoActivity() {
        val intent = Intent(this, AltaResultadoActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToAltaEquipoActivity() {
        val intent = Intent(this, AltaEquipoActivity::class.java)
        startActivity(intent)
    }

}
