package com.example.aprendizajeapp.model

import com.example.aprendizajeapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MODEL – autenticación simple contra MockAPI (colección /usuarios).
 */
class UsuarioRepository {

    private val api = RetrofitClient.api

    fun login(email: String, password: String, onSuccess: (Usuario) -> Unit, onError: (String) -> Unit) {
        api.buscarUsuarioPorEmail(email).enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (!response.isSuccessful) {
                    onError("Error al consultar usuarios")
                    return
                }
                val lista = response.body() ?: emptyList()
                val user = lista.firstOrNull { it.email.equals(email, true) && it.password == password }
                if (user != null) onSuccess(user)
                else onError("Correo o contraseña incorrectos")
            }
            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                onError(t.message ?: "Error de conexión")
            }
        })
    }

    fun registrar(usuario: Usuario, onSuccess: (Usuario) -> Unit, onError: (String) -> Unit) {
        // Verificar si el email ya existe
        api.buscarUsuarioPorEmail(usuario.email).enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                val existentes = response.body() ?: emptyList()
                if (existentes.any { it.email.equals(usuario.email, true) }) {
                    onError("Este correo ya está registrado")
                    return
                }
                api.registrarUsuario(usuario).enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful && response.body() != null) {
                            onSuccess(response.body()!!)
                        } else {
                            onError("No se pudo registrar el usuario")
                        }
                    }
                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        onError(t.message ?: "Error de conexión")
                    }
                })
            }
            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                onError(t.message ?: "Error de conexión")
            }
        })
    }
}
