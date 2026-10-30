package com.example.aprendizajeapp.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aprendizajeapp.R
import com.example.aprendizajeapp.controller.AuthController
import com.example.aprendizajeapp.databinding.ActivityRegisterBinding
import com.example.aprendizajeapp.util.SessionManager
import com.example.aprendizajeapp.view.recurso.ListaRecursosActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var session: SessionManager
    private val controller = AuthController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val roles = resources.getStringArray(R.array.roles)
        binding.spinnerRol.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        binding.btnRegister.setOnClickListener { intentarRegistro() }
        binding.tvGoLogin.setOnClickListener { finish() }
    }

    private fun intentarRegistro() {
        val nombre = binding.etNombre.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirm = binding.etConfirmPassword.text.toString()
        val rol = binding.spinnerRol.selectedItem.toString()

        showLoading(true)
        controller.registrar(nombre, email, password, confirm, rol,
            onSuccess = { usuario ->
                showLoading(false)
                session.saveSession(usuario)
                Toast.makeText(this, "Cuenta creada. Bienvenido, ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ListaRecursosActivity::class.java))
                finishAffinity()
            },
            onError = { msg ->
                showLoading(false)
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !show
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
