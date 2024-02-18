package com.example.prctica_bbdd_room.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.database.database.entidades.Equipo

class EquipoAdapter(private var equipos: List<Equipo>, private val onItemClick: (Equipo) -> Unit) :
    RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    inner class EquipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEquipoTextView: TextView = itemView.findViewById(R.id.nombreEquipoTextView)
        val ciudadTextView: TextView = itemView.findViewById(R.id.ciudadTextView)
        val pabellonTextView: TextView = itemView.findViewById(R.id.pabellonTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val equipo = equipos[position]
                    onItemClick(equipo)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = equipos[position]
        holder.nombreEquipoTextView.text = equipo.nombreEqui
        holder.ciudadTextView.text = equipo.ciudad
        holder.pabellonTextView.text = equipo.nombrePabellon
    }

    override fun getItemCount(): Int {
        return equipos.size
    }

    fun setItems(newList: List<Equipo>) {
        equipos = newList
        notifyDataSetChanged()
    }
}



