package com.example.aprendizajeapp.view.recurso

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aprendizajeapp.R
import com.example.aprendizajeapp.controller.RecursoController
import com.example.aprendizajeapp.databinding.ActivityFormRecursoBinding
import com.example.aprendizajeapp.util.SessionManager

class FormRecursoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormRecursoBinding
    private lateinit var session: SessionManager
    private val controller = RecursoController()
    private var recursoId: String? = null
    private var promedio = 0.0
    private var total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormRecursoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        if (!session.isDocente()) {
            Toast.makeText(this, R.string.solo_docente, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tipos = resources.getStringArray(R.array.tipos_recurso)
        binding.spinnerTipo.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        recursoId = intent.getStringExtra("RECURSO_ID")
        if (recursoId != null) {
            supportActionBar?.title = getString(R.string.edit_recurso)
            binding.btnGuardar.text = getString(R.string.actualizar)
            binding.etTitulo.setText(intent.getStringExtra("TITULO"))
            binding.etDescripcion.setText(intent.getStringExtra("DESCRIPCION"))
            binding.etEnlace.setText(intent.getStringExtra("ENLACE"))
            binding.etImagen.setText(intent.getStringExtra("IMAGEN"))
            promedio = intent.getDoubleExtra("PROMEDIO", 0.0)
            total = intent.getIntExtra("TOTAL", 0)
            val tipo = intent.getStringExtra("TIPO") ?: ""
            val idx = tipos.indexOfFirst { it.equals(tipo, true) }
            if (idx >= 0) binding.spinnerTipo.setSelection(idx)
        } else {
            supportActionBar?.title = getString(R.string.add_recurso)
        }

        binding.btnGuardar.setOnClickListener { guardar() }
    }

    private fun guardar() {
        val titulo = binding.etTitulo.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val tipo = binding.spinnerTipo.selectedItem.toString()
        val enlace = binding.etEnlace.text.toString()
        val imagen = binding.etImagen.text.toString()

        showLoading(true)
        if (recursoId == null) {
            controller.crear(titulo, descripcion, tipo, enlace, imagen,
                onSuccess = {
                    showLoading(false)
                    Toast.makeText(this, R.string.exito_guardar, Toast.LENGTH_SHORT).show()
                    finish()
                },
                onError = { msg ->
                    showLoading(false)
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            )
        } else {
            controller.actualizar(recursoId!!, titulo, descripcion, tipo, enlace, imagen, promedio, total,
                onSuccess = {
                    showLoading(false)
                    Toast.makeText(this, R.string.exito_actualizar, Toast.LENGTH_SHORT).show()
                    finish()
                },
                onError = { msg ->
                    showLoading(false)
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnGuardar.isEnabled = !show
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
