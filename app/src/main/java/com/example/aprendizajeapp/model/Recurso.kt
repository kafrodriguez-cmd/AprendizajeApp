package com.example.aprendizajeapp.model

data class Recurso(
    val id: String? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val tipo: String = "", // libro, video, articulo, tutorial
    val enlace: String = "",
    val imagen: String = "",
    val promedioRating: Double = 0.0,
    val totalRatings: Int = 0
)
