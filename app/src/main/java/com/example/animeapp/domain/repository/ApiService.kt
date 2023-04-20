package com.example.animeapp.domain.repository


import com.example.animeapp.domain.CharactersModel.CharactersModel
import com.example.animeapp.domain.detailModel.AnimeDetailModel
import com.example.animeapp.domain.searchModel.AnimeSearchModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.jikan.moe/"
private const val GET_DETAILS_URL = "https://api.jikan.moe/v4/anime/"


interface ApiService {


    @GET("${BASE_URL}v4/anime")    // (в конце добавить лимит limit=20)
    suspend fun getAnimeSearchByName(@Query("q") nameOfAnime: String): Response<AnimeSearchModel>

    @GET("$GET_DETAILS_URL{id}/full")
    suspend fun getDetailsFromAnime(@Path("id") id: Int): Response<AnimeDetailModel>

    @GET("$GET_DETAILS_URL{id}/characters")
    suspend fun getCharactersFromId(@Path("id") id: Int): Response<CharactersModel>


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
