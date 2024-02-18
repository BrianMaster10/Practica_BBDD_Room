import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prctica_bbdd_room.R
import com.example.prctica_bbdd_room.database.database.entidades.Clasificacion

class ClasificacionAdapter(private val clasificaciones: MutableList<Clasificacion>) : RecyclerView.Adapter<ClasificacionAdapter.ClasificacionViewHolder>() {

    inner class ClasificacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val puntosTextView: TextView = itemView.findViewById(R.id.puntosTextView)
        private val nombreEquipoTextView: TextView = itemView.findViewById(R.id.nombreEquipoTextView)

        fun bind(clasificacion: Clasificacion) {
            puntosTextView.text = clasificacion.puntos.toString()
            nombreEquipoTextView.text = clasificacion.nombreEquiFK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClasificacionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_clasificacion, parent, false)
        return ClasificacionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClasificacionViewHolder, position: Int) {
        val clasificacion = clasificaciones[position]
        holder.bind(clasificacion)
    }

    override fun getItemCount(): Int {
        return clasificaciones.size
    }

    fun actualizarClasificaciones(nuevaLista: List<Clasificacion>) {
        clasificaciones.clear()
        clasificaciones.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}




