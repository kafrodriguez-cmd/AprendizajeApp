package com.example.aprendizajeapp.model

data class Usuario(
    val id: String? = null,
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val rol: String = "estudiante" // "estudiante" | "docente"
)
