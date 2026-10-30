package com.example.aprendizajeapp.view.recurso

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aprendizajeapp.R
import com.example.aprendizajeapp.controller.RecursoController
import com.example.aprendizajeapp.databinding.ActivityListaRecursosBinding
import com.example.aprendizajeapp.model.Recurso
import com.example.aprendizajeapp.util.FavoritosManager
import com.example.aprendizajeapp.util.SessionManager
import com.example.aprendizajeapp.view.auth.LoginActivity
import com.example.aprendizajeapp.view.favoritos.FavoritosActivity

class ListaRecursosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaRecursosBinding
    private lateinit var session: SessionManager
    private lateinit var favoritos: FavoritosManager
    private lateinit var adapter: RecursoAdapter
    private val controller = RecursoController()
    private var listaCompleta: List<Recurso> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaRecursosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        favoritos = FavoritosManager(this)

        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupToolbar()
        setupFiltros()
        setupRecycler()
        setupFab()

        binding.swipeRefresh.setOnRefreshListener { cargar() }
        cargar()
    }

    private fun setupToolbar() {
        binding.toolbar.subtitle = "${session.getNombre()} (${session.getRol()})"
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_favoritos -> {
                    startActivity(Intent(this, FavoritosActivity::class.java))
                    true
                }
                R.id.action_logout -> {
                    session.clearSession()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupFiltros() {
        val tipos = resources.getStringArray(R.array.filtro_tipos)
        binding.spinnerFiltro.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = aplicarFiltro()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.spinnerFiltro.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long
                ) = aplicarFiltro()
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
    }

    private fun setupRecycler() {
        val userId = session.getUserId() ?: ""
        adapter = RecursoAdapter(
            esDocente = session.isDocente(),
            esFavorito = { id -> favoritos.esFavorito(userId, id) },
            onFavorito = { r ->
                val id = r.id ?: return@RecursoAdapter
                val ahora = favoritos.toggle(userId, id)
                Toast.makeText(
                    this,
                    if (ahora) R.string.exito_favorito else R.string.quitar_favorito,
                    Toast.LENGTH_SHORT
                ).show()
                adapter.notifyDataSetChanged()
            },
            onEditar = { r ->
                val i = Intent(this, FormRecursoActivity::class.java)
                i.putExtra("RECURSO_ID", r.id)
                i.putExtra("TITULO", r.titulo)
                i.putExtra("DESCRIPCION", r.descripcion)
                i.putExtra("TIPO", r.tipo)
                i.putExtra("ENLACE", r.enlace)
                i.putExtra("IMAGEN", r.imagen)
                i.putExtra("PROMEDIO", r.promedioRating)
                i.putExtra("TOTAL", r.totalRatings)
                startActivity(i)
            },
            onEliminar = { r -> confirmarEliminar(r) },
            onClick = { r -> mostrarDetalle(r) }
        )
        binding.rvRecursos.layoutManager = LinearLayoutManager(this)
        binding.rvRecursos.adapter = adapter
    }

    private fun setupFab() {
        if (session.isDocente()) {
            binding.fabAdd.visibility = View.VISIBLE
            binding.fabAdd.setOnClickListener {
                startActivity(Intent(this, FormRecursoActivity::class.java))
            }
        }
    }

    private fun cargar() {
        showLoading(true)
        controller.listar(
            onSuccess = { lista ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                listaCompleta = lista
                aplicarFiltro()
            },
            onError = { msg ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun aplicarFiltro() {
        val query = binding.etBuscar.text?.toString() ?: ""
        val tipo = binding.spinnerFiltro.selectedItem?.toString()
        val filtrada = controller.buscar(listaCompleta, query, tipo)
        adapter.submitList(filtrada)
        binding.tvEmpty.visibility = if (filtrada.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun confirmarEliminar(r: Recurso) {
        AlertDialog.Builder(this)
            .setTitle(R.string.eliminar)
            .setMessage(R.string.confirmar_eliminar)
            .setPositiveButton(R.string.si) { _, _ ->
                val id = r.id ?: return@setPositiveButton
                showLoading(true)
                controller.eliminar(id,
                    onSuccess = {
                        showLoading(false)
                        Toast.makeText(this, R.string.exito_eliminar, Toast.LENGTH_SHORT).show()
                        cargar()
                    },
                    onError = { msg ->
                        showLoading(false)
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    }
                )
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun mostrarDetalle(r: Recurso) {
        val opciones = mutableListOf(
            getString(R.string.abrir_enlace),
            getString(R.string.calificar)
        )
        AlertDialog.Builder(this)
            .setTitle(r.titulo)
            .setMessage(r.descripcion)
            .setItems(opciones.toTypedArray()) { _, which ->
                when (which) {
                    0 -> {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(r.enlace)))
                        } catch (e: Exception) {
                            Toast.makeText(this, "No se pudo abrir el enlace", Toast.LENGTH_SHORT).show()
                        }
                    }
                    1 -> mostrarDialogoRating(r)
                }
            }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }

    private fun mostrarDialogoRating(r: Recurso) {
        val estrellas = arrayOf("1 ★", "2 ★", "3 ★", "4 ★", "5 ★")
        AlertDialog.Builder(this)
            .setTitle(R.string.calificar)
            .setItems(estrellas) { _, which ->
                val valor = which + 1
                showLoading(true)
                controller.calificar(r, valor,
                    onSuccess = {
                        showLoading(false)
                        Toast.makeText(this, R.string.exito_rating, Toast.LENGTH_SHORT).show()
                        cargar()
                    },
                    onError = { msg ->
                        showLoading(false)
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    }
                )
            }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) cargar()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
