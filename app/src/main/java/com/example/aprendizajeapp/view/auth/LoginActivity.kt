package com.example.aprendizajeapp.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aprendizajeapp.controller.AuthController
import com.example.aprendizajeapp.databinding.ActivityLoginBinding
import com.example.aprendizajeapp.util.SessionManager
import com.example.aprendizajeapp.view.recurso.ListaRecursosActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager
    private val controller = AuthController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        if (session.isLoggedIn()) {
            irACatalogo()
            return
        }

        binding.btnLogin.setOnClickListener { intentarLogin() }
        binding.tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun intentarLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        showLoading(true)
        controller.login(email, password,
            onSuccess = { usuario ->
                showLoading(false)
                session.saveSession(usuario)
                Toast.makeText(this, "Bienvenido, ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                irACatalogo()
            },
            onError = { msg ->
                showLoading(false)
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun irACatalogo() {
        startActivity(Intent(this, ListaRecursosActivity::class.java))
        finish()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !show
    }
}
