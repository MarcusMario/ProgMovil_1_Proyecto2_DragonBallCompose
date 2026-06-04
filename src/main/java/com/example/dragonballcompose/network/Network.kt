package com.example.dragonballcompose.network

import com.example.dragonballcompose.models.Character
import com.example.dragonballcompose.models.CharactersResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface DragonBallApiService {

    // Sin filtro → devuelve { items:[...], meta:{...} }
    @GET("characters")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<CharactersResponse>

    // Con filtro name → devuelve array directo [...] según documentación
    @GET("characters")
    suspend fun searchCharacterByName(
        @Query("name") name: String
    ): Response<ResponseBody>

    // Por ID → devuelve objeto Character con originPlanet y transformations
    @GET("characters/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Response<Character>
}

object RetrofitClient {

    // URL correcta según documentación oficial: https://dragonball-api.com/api
    private const val BASE_URL = "https://dragonball-api.com/api/"

    private val rawInterceptor = okhttp3.Interceptor { chain ->
        val request = chain.request()
        android.util.Log.d("API_URL", "Request: ${request.url}")
        chain.proceed(request)
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(rawInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder().setLenient().create()

    val apiService: DragonBallApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(DragonBallApiService::class.java)
}
