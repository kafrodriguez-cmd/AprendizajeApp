package com.example.aprendizajeapp.controller

import com.example.aprendizajeapp.model.Recurso
import com.example.aprendizajeapp.model.RecursoRepository

/**
 * CONTROLLER – intermedia entre la View y el Model de recursos.
 * Aquí también se aplican validaciones de negocio.
 */
class RecursoController {

    private val repository = RecursoRepository()

    fun listar(onSuccess: (List<Recurso>) -> Unit, onError: (String) -> Unit) {
        repository.obtenerTodos(onSuccess, onError)
    }

    fun buscar(
        lista: List<Recurso>,
        query: String,
        tipoFiltro: String?
    ): List<Recurso> {
        val q = query.trim().lowercase()
        return lista.filter { r ->
            val matchQuery = q.isEmpty() ||
                    r.titulo.lowercase().contains(q) ||
                    r.descripcion.lowercase().contains(q) ||
                    r.id?.contains(q) == true ||
                    r.tipo.lowercase().contains(q)
            val matchTipo = tipoFiltro.isNullOrBlank() ||
                    tipoFiltro == "Todos" ||
                    r.tipo.equals(tipoFiltro, true)
            matchQuery && matchTipo
        }
    }

    fun crear(
        titulo: String,
        descripcion: String,
        tipo: String,
        enlace: String,
        imagen: String,
        onSuccess: (Recurso) -> Unit,
        onError: (String) -> Unit
    ) {
        val error = validar(titulo, descripcion, tipo, enlace)
        if (error != null) {
            onError(error)
            return
        }
        val recurso = Recurso(
            titulo = titulo.trim(),
            descripcion = descripcion.trim(),
            tipo = tipo.trim().lowercase(),
            enlace = enlace.trim(),
            imagen = imagen.trim()
        )
        repository.crear(recurso, onSuccess, onError)
    }

    fun actualizar(
        id: String,
        titulo: String,
        descripcion: String,
        tipo: String,
        enlace: String,
        imagen: String,
        promedio: Double,
        total: Int,
        onSuccess: (Recurso) -> Unit,
        onError: (String) -> Unit
    ) {
        val error = validar(titulo, descripcion, tipo, enlace)
        if (error != null) {
            onError(error)
            return
        }
        val recurso = Recurso(
            id = id,
            titulo = titulo.trim(),
            descripcion = descripcion.trim(),
            tipo = tipo.trim().lowercase(),
            enlace = enlace.trim(),
            imagen = imagen.trim(),
            promedioRating = promedio,
            totalRatings = total
        )
        repository.actualizar(id, recurso, onSuccess, onError)
    }

    fun eliminar(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        repository.eliminar(id, onSuccess, onError)
    }

    /** Actualiza el promedio de rating en el backend */
    fun calificar(
        recurso: Recurso,
        estrellas: Int,
        onSuccess: (Recurso) -> Unit,
        onError: (String) -> Unit
    ) {
        if (estrellas !in 1..5) {
            onError("La calificación debe ser entre 1 y 5")
            return
        }
        val id = recurso.id ?: return onError("Recurso sin ID")
        val total = recurso.totalRatings + 1
        val nuevoPromedio = ((recurso.promedioRating * recurso.totalRatings) + estrellas) / total
        val actualizado = recurso.copy(
            promedioRating = String.format("%.1f", nuevoPromedio).toDouble(),
            totalRatings = total
        )
        repository.actualizar(id, actualizado, onSuccess, onError)
    }

    private fun validar(titulo: String, descripcion: String, tipo: String, enlace: String): String? {
        if (titulo.isBlank() || descripcion.isBlank() || tipo.isBlank() || enlace.isBlank()) {
            return "Todos los campos son obligatorios"
        }
        if (tipo == "Seleccione un tipo") return "Seleccione un tipo de recurso"
        if (descripcion.length < 15) return "La descripción debe tener al menos 15 caracteres"
        if (!enlace.startsWith("http")) return "El enlace debe ser una URL válida (http/https)"
        return null
    }
}
