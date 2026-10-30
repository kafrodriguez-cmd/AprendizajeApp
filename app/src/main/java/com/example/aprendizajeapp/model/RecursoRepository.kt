package com.example.aprendizajeapp.model

import com.example.aprendizajeapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MODEL – acceso a datos de recursos (API).
 * El Controller llama a este repository; la View no habla directo con Retrofit.
 */
class RecursoRepository {

    private val api = RetrofitClient.api

    fun obtenerTodos(onSuccess: (List<Recurso>) -> Unit, onError: (String) -> Unit) {
        api.obtenerRecursos().enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: emptyList())
                } else {
                    onError("Error ${response.code()}: no se pudieron cargar los recursos")
                }
            }
            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                onError(t.message ?: "Error de conexión")
            }
        })
    }

    fun crear(recurso: Recurso, onSuccess: (Recurso) -> Unit, onError: (String) -> Unit) {
        api.crearRecurso(recurso).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError("No se pudo crear el recurso")
                }
            }
            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                onError(t.message ?: "Error de conexión")
            }
        })
    }

    fun actualizar(id: String, recurso: Recurso, onSuccess: (Recurso) -> Unit, onError: (String) -> Unit) {
        api.actualizarRecurso(id, recurso).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError("No se pudo actualizar el recurso")
                }
            }
            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                onError(t.message ?: "Error de conexión")
            }
        })
    }

    fun eliminar(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        api.eliminarRecurso(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) onSuccess()
                else onError("No se pudo eliminar el recurso")
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError(t.message ?: "Error de conexión")
            }
        })
    }
}
