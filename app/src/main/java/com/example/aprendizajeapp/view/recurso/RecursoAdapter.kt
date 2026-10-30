package com.example.aprendizajeapp.view.recurso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aprendizajeapp.R
import com.example.aprendizajeapp.databinding.ItemRecursoBinding
import com.example.aprendizajeapp.model.Recurso

class RecursoAdapter(
    private val esDocente: Boolean,
    private val esFavorito: (String) -> Boolean,
    private val onFavorito: (Recurso) -> Unit,
    private val onEditar: (Recurso) -> Unit,
    private val onEliminar: (Recurso) -> Unit,
    private val onClick: (Recurso) -> Unit
) : ListAdapter<Recurso, RecursoAdapter.VH>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemRecursoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemRecursoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(r: Recurso) {
            b.tvTitulo.text = r.titulo
            b.tvTipo.text = r.tipo.uppercase()
            b.tvDescripcion.text = r.descripcion
            b.tvRating.text = "★ ${r.promedioRating} (${r.totalRatings})"

            Glide.with(b.root)
                .load(r.imagen)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .centerCrop()
                .into(b.ivImagen)

            val fav = r.id != null && esFavorito(r.id)
            b.btnFavorito.text = if (fav)
                b.root.context.getString(R.string.quitar_favorito)
            else
                b.root.context.getString(R.string.agregar_favorito)

            b.btnFavorito.setOnClickListener { onFavorito(r) }
            b.root.setOnClickListener { onClick(r) }

            if (esDocente) {
                b.btnEditar.visibility = View.VISIBLE
                b.btnEliminar.visibility = View.VISIBLE
                b.btnEditar.setOnClickListener { onEditar(r) }
                b.btnEliminar.setOnClickListener { onEliminar(r) }
            } else {
                b.btnEditar.visibility = View.GONE
                b.btnEliminar.visibility = View.GONE
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<Recurso>() {
        override fun areItemsTheSame(a: Recurso, b: Recurso) = a.id == b.id
        override fun areContentsTheSame(a: Recurso, b: Recurso) = a == b
    }
}
