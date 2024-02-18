package com.example.prctica_bbdd_room.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.database.database.entidades.Jugador

class JugadorAdapter(private var jugadores: MutableList<Jugador>) : RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>() {

    inner class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreJugadorTextView: TextView = itemView.findViewById(R.id.nombreJugadorTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_jugador, parent, false)
        return JugadorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.nombreJugadorTextView.text = jugador.nombre
    }

    override fun getItemCount(): Int {
        return jugadores.size
    }

    // Método para actualizar la lista de jugadores
    fun updateItems(newList: List<Jugador>) {
        jugadores.clear()
        jugadores.addAll(newList)
        notifyDataSetChanged()
    }

    // Método para agregar un nuevo jugador a la lista
    fun agregarJugador(jugador: Jugador) {
        jugadores.add(jugador)
        notifyItemInserted(jugadores.size - 1)
    }
}



