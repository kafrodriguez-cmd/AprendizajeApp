package com.example.aprendizajeapp.network

import com.example.aprendizajeapp.model.Recurso
import com.example.aprendizajeapp.model.Usuario
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface Retrofit – igual que en Guías 11 y 12.
 * Endpoints de MockAPI.io:
 *   /usuarios
 *   /recursos
 */
interface ApiService {

    // ---------- USUARIOS (Auth simple) ----------
    @GET("usuarios")
    fun obtenerUsuarios(): Call<List<Usuario>>

    @GET("usuarios")
    fun buscarUsuarioPorEmail(@Query("email") email: String): Call<List<Usuario>>

    @POST("usuarios")
    fun registrarUsuario(@Body usuario: Usuario): Call<Usuario>

    // ---------- RECURSOS (CRUD) ----------
    @GET("recursos")
    fun obtenerRecursos(): Call<List<Recurso>>

    @GET("recursos/{id}")
    fun obtenerRecursoPorId(@Path("id") id: String): Call<Recurso>

    @POST("recursos")
    fun crearRecurso(@Body recurso: Recurso): Call<Recurso>

    @PUT("recursos/{id}")
    fun actualizarRecurso(@Path("id") id: String, @Body recurso: Recurso): Call<Recurso>

    @DELETE("recursos/{id}")
    fun eliminarRecurso(@Path("id") id: String): Call<Void>
}
