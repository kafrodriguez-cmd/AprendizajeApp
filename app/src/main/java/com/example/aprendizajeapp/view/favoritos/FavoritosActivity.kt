package com.example.aprendizajeapp.view.favoritos

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aprendizajeapp.R
import com.example.aprendizajeapp.controller.RecursoController
import com.example.aprendizajeapp.databinding.ActivityListaRecursosBinding
import com.example.aprendizajeapp.util.FavoritosManager
import com.example.aprendizajeapp.util.SessionManager
import com.example.aprendizajeapp.view.recurso.RecursoAdapter

class FavoritosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaRecursosBinding
    private lateinit var session: SessionManager
    private lateinit var favoritos: FavoritosManager
    private lateinit var adapter: RecursoAdapter
    private val controller = RecursoController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaRecursosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        favoritos = FavoritosManager(this)

        binding.toolbar.title = getString(R.string.favoritos_title)
        binding.fabAdd.visibility = View.GONE
        binding.etBuscar.visibility = View.GONE
        binding.spinnerFiltro.visibility = View.GONE

        val userId = session.getUserId() ?: ""

        adapter = RecursoAdapter(
            esDocente = false,
            esFavorito = { id -> favoritos.esFavorito(userId, id) },
            onFavorito = { r ->
                val id = r.id ?: return@RecursoAdapter
                favoritos.toggle(userId, id)
                cargar(userId)
            },
            onEditar = {},
            onEliminar = {},
            onClick = {}
        )

        binding.rvRecursos.layoutManager = LinearLayoutManager(this)
        binding.rvRecursos.adapter = adapter

        cargar(userId)
    }

    private fun cargar(userId: String) {
        binding.progressBar.visibility = View.VISIBLE
        controller.listar(
            onSuccess = { lista ->
                binding.progressBar.visibility = View.GONE
                val ids = favoritos.getFavoritos(userId)
                val favs = lista.filter { it.id != null && ids.contains(it.id) }
                adapter.submitList(favs)
                binding.tvEmpty.text = getString(R.string.sin_favoritos)
                binding.tvEmpty.visibility = if (favs.isEmpty()) View.VISIBLE else View.GONE
            },
            onError = { msg ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        )
    }
}