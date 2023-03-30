package com.example.animeapp.domain.repository


import com.example.animeapp.domain.searchModel.AnimeSearchModel
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.jikan.moe/"


interface ApiService {

//    @GET("v4/anime")
//    suspend fun getAnimeModel(): Response<AnimeSearchModel>

    @GET("$BASE_URL/v4/anime?")    // (в конце добавить лимит limit=20)
    suspend fun getAnimeSearchByName(@Query("q") nameOfAnime: String): Response<AnimeSearchModel>

//    @GET("search/anime")
//    suspend fun searchAnime(@Query("q") query: String): Response<AnimeSearchModel>


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
