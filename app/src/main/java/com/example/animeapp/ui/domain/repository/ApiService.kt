package com.example.animeapp.ui.domain.repository


import com.example.animeapp.ui.domain.models.searchModel.AnimeSearchModel

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.jikan.moe/"


interface ApiService {

    @GET("v4/anime")
    suspend fun getAnimeModel(): Response<AnimeSearchModel>

//    @GET("https://api.jikan.moe/v4/anime?q={nameOfAnime}&sfw&limit=10")    // TODO: СЮДА ТОЖЕ (в конце добавить лимит limit=20) }&sfw"
//    suspend fun getAnimeSearchByName(@Path("nameOfAnime") nameOfAnime: String): Response<>                       // TODO: вставить сюда название класса и вставить еще потом модель для вывода данных




//    @GET("v4/anime")
//    fun getData(): Call<Data>


    companion object {

        private val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)


        private val httpClient = OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .build()


        val api: ApiService by lazy {

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
                .create(ApiService::class.java)
        }
    }


}
