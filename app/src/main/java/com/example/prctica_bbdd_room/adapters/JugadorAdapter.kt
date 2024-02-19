package com.example.prctica_bbdd_room.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.database.database.entidades.Jugador

class JugadorAdapter(private var jugadores: MutableList<Jugador>) : RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>() {

    private var onItemClickListener: ((Jugador) -> Unit)? = null
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreJugadorTextView: TextView = itemView.findViewById(R.id.nombreJugadorTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(jugadores[position])
                }
            }
        }
    }
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Actualiza la vista para reflejar el cambio en la selección
    }

    fun getSelectedJugador(): Jugador? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            jugadores[selectedPosition]
        } else {
            null
        }
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

    fun updateItems(newList: List<Jugador>) {
        jugadores.clear()
        jugadores.addAll(newList)
        notifyDataSetChanged()
    }

    fun agregarJugador(jugador: Jugador) {
        jugadores.add(jugador)
        notifyItemInserted(jugadores.size - 1)
    }

    fun eliminarJugador(jugador: Jugador) {
        val position = jugadores.indexOf(jugador)
        if (position != -1) {
            jugadores.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun setOnItemClickListener(listener: (Jugador) -> Unit) {
        onItemClickListener = listener
    }

}
