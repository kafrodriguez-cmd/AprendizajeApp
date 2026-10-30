package com.example.aprendizajeapp.util

object Constants {
    // ⚠️ Reemplaza con TU URL de MockAPI.io
    // Ejemplo: https://XXXXX.mockapi.io/api/
    const val BASE_URL = "https://6ae26992bbb541292299eee6.mockapi.io/"

    const val PREFS_NAME = "aprendizaje_prefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_NOMBRE = "user_nombre"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_ROL = "user_rol"
    const val KEY_LOGGED_IN = "logged_in"

    const val ROL_ESTUDIANTE = "estudiante"
    const val ROL_DOCENTE = "docente"

    val TIPOS_RECURSO = listOf(
        "Seleccione un tipo",
        "libro",
        "video",
        "articulo",
        "tutorial"
    )
}
