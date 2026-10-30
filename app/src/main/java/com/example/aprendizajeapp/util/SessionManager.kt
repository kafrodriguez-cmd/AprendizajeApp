package com.example.aprendizajeapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.aprendizajeapp.model.Usuario

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(usuario: Usuario) {
        prefs.edit().apply {
            putBoolean(Constants.KEY_LOGGED_IN, true)
            putString(Constants.KEY_USER_ID, usuario.id)
            putString(Constants.KEY_USER_NOMBRE, usuario.nombre)
            putString(Constants.KEY_USER_EMAIL, usuario.email)
            putString(Constants.KEY_USER_ROL, usuario.rol)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(Constants.KEY_LOGGED_IN, false)

    fun getUserId(): String? = prefs.getString(Constants.KEY_USER_ID, null)

    fun getNombre(): String? = prefs.getString(Constants.KEY_USER_NOMBRE, null)

    fun getEmail(): String? = prefs.getString(Constants.KEY_USER_EMAIL, null)

    fun getRol(): String = prefs.getString(Constants.KEY_USER_ROL, Constants.ROL_ESTUDIANTE)
        ?: Constants.ROL_ESTUDIANTE

    fun isDocente(): Boolean = getRol() == Constants.ROL_DOCENTE

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
