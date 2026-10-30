package com.example.aprendizajeapp.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Persistencia local de favoritos del estudiante (SharedPreferences).
 * Guarda los IDs de recursos marcados como favoritos.
 */
class FavoritosManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("favoritos_prefs", Context.MODE_PRIVATE)

    private fun key(userId: String) = "fav_$userId"

    fun getFavoritos(userId: String): MutableSet<String> {
        return prefs.getStringSet(key(userId), emptySet())?.toMutableSet() ?: mutableSetOf()
    }

    fun esFavorito(userId: String, recursoId: String): Boolean {
        return getFavoritos(userId).contains(recursoId)
    }

    fun toggle(userId: String, recursoId: String): Boolean {
        val set = getFavoritos(userId)
        val ahoraEsFavorito = if (set.contains(recursoId)) {
            set.remove(recursoId)
            false
        } else {
            set.add(recursoId)
            true
        }
        prefs.edit().putStringSet(key(userId), set).apply()
        return ahoraEsFavorito
    }
}
