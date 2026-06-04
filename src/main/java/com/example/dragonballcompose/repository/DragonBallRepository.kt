package com.example.dragonballcompose.repository

import android.util.Log
import com.example.dragonballcompose.models.Character
import com.example.dragonballcompose.network.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

class DragonBallRepository {

    private val api = RetrofitClient.apiService
    private val gson = Gson()

    suspend fun searchCharacter(name: String): Result<List<Character>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchCharacterByName(name)
                val rawString = response.body()?.string()
                    ?: response.errorBody()?.string()
                    ?: ""

                Log.d("API_RAW", "Respuesta: $rawString")

                if (rawString.isBlank()) {
                    return@withContext Result.Error("Sin respuesta del servidor")
                }

                val trimmed = rawString.trim()

                when {
                    // Filtros devuelven array directo según documentación
                    trimmed.startsWith("[") -> {
                        val type = object : TypeToken<List<Character>>() {}.type
                        val list: List<Character> = gson.fromJson(trimmed, type)
                        if (list.isEmpty()) {
                            Result.Error("No se encontró \"$name\"")
                        } else {
                            Result.Success(list)
                        }
                    }
                    // Sin filtro devuelve { items: [...] }
                    trimmed.startsWith("{") -> {
                        val jsonObj = gson.fromJson(trimmed, JsonObject::class.java)
                        if (jsonObj.has("items")) {
                            val type = object : TypeToken<List<Character>>() {}.type
                            val list: List<Character> = gson.fromJson(
                                jsonObj.getAsJsonArray("items"), type
                            )
                            if (list.isEmpty()) {
                                Result.Error("No se encontró \"$name\"")
                            } else {
                                Result.Success(list)
                            }
                        } else {
                            Result.Error("Respuesta inesperada del servidor")
                        }
                    }
                    else -> {
                        Log.e("API_RAW", "No es JSON: $trimmed")
                        Result.Error("URL incorrecta. Respuesta: ${trimmed.take(100)}")
                    }
                }
            } catch (e: Exception) {
                Log.e("API", "Excepción: ${e.message}", e)
                Result.Error("Error: ${e.message ?: "Verifica tu conexión"}")
            }
        }
    }

    suspend fun getCharacterById(id: Int): Result<Character> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCharacterById(id)
                if (response.isSuccessful && response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error("Personaje no encontrado: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Excepción detalle: ${e.message}", e)
                Result.Error("Error: ${e.message}")
            }
        }
    }
}
