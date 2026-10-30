package com.example.aprendizajeapp.controller

import com.example.aprendizajeapp.model.Usuario
import com.example.aprendizajeapp.model.UsuarioRepository

/**
 * CONTROLLER – intermedia entre la View (Login/Register) y el Model (UsuarioRepository).
 */
class AuthController {

    private val repository = UsuarioRepository()

    fun login(
        email: String,
        password: String,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onError("Todos los campos son obligatorios")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError("Correo electrónico no válido")
            return
        }
        repository.login(email.trim(), password, onSuccess, onError)
    }

    fun registrar(
        nombre: String,
        email: String,
        password: String,
        confirmPassword: String,
        rol: String,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    ) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            onError("Todos los campos son obligatorios")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError("Correo electrónico no válido")
            return
        }
        if (password.length < 6) {
            onError("La contraseña debe tener al menos 6 caracteres")
            return
        }
        if (password != confirmPassword) {
            onError("Las contraseñas no coinciden")
            return
        }
        val usuario = Usuario(
            nombre = nombre.trim(),
            email = email.trim().lowercase(),
            password = password,
            rol = rol
        )
        repository.registrar(usuario, onSuccess, onError)
    }
}
