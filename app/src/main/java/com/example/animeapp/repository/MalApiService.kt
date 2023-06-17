package com.example.animeapp.repository


import android.util.Log
import com.example.animeapp.domain.models.castModel.CastModel
import com.example.animeapp.domain.models.characterModel.CharacterFullModel
import com.example.animeapp.domain.models.characterPictures.CharacterPicturesModel
import com.example.animeapp.domain.models.detailModel.AnimeDetailModel
import com.example.animeapp.domain.models.newAnimeSearchModel.NewAnimeSearchModel
import com.example.animeapp.domain.models.staffMemberFullModel.StaffMemberFullModel
import com.example.animeapp.domain.models.staffModel.StaffModel
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.jikan.moe/"
private const val BASE_URL_FOR_CHARACTER = "https://api.jikan.moe/v4/"
private const val GET_DETAILS_URL = "https://api.jikan.moe/v4/anime/"


interface MalApiService {


    @GET("${BASE_URL}v4/anime")
    suspend fun getAnimeSearchByName(
        @Query("Sfw") sfw: Boolean = true,
        @Query("page") page: Int = 1,
        @Query("q") nameOfAnime: String,
//        @Query("type") type: String,
//        @Query("min_score") min_score: Float,
//        @Query("max_score") max_score: Float,
//        @Query("rating") rating: String,
//        @Query("sfw") sfw: Boolean,
//        @Query("genres") genres: String,
//        @Query("sort") sort: String,
//        @Query("start_date") start_date: String,
//        @Query("end_date") end_date: String,
    ): Response<NewAnimeSearchModel>

    //    @GET("${BASE_URL}v4/anime")
//    suspend fun getAnimeSearchByName(
//        @Query("page") page: Int,
//        @Query("q") nameOfAnime: String,
//        @Header("Cache-Control") cacheControl: CacheControl // Add cache control header
//    ): Response<NewAnimeSearchModel>
    @GET("$GET_DETAILS_URL{id}/full")
    suspend fun getDetailsFromAnime(@Path("id") id: Int): Response<AnimeDetailModel>

    @GET("${BASE_URL}v4/random/anime")
    suspend fun getRandomAnime(): Response<AnimeDetailModel>

    @GET("$GET_DETAILS_URL{id}/characters")
    suspend fun getCharactersFromId(@Path("id") id: Int): Response<CastModel> {
        try {
            return api.getCharactersFromId(id)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return Response.success(null)
            }
            throw e
        }
    }

    @GET("${BASE_URL_FOR_CHARACTER}characters/{id}/full")
    suspend fun getCharacterFullFromId(@Path("id") id: Int): Response<CharacterFullModel>

    @GET("${BASE_URL_FOR_CHARACTER}characters/{id}/pictures")
    suspend fun getCharacterFullPictures(@Path("id") id: Int): Response<CharacterPicturesModel>

    @GET("$GET_DETAILS_URL{id}/staff")
    suspend fun getStaffFromId(@Path("id") id: Int): Response<StaffModel> {
        try {
            return api.getStaffFromId(id)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return Response.success(null)
            }
            throw e
        }
    }

    @GET("${BASE_URL_FOR_CHARACTER}people/{id}/full")
    suspend fun getStaffFullFromId(@Path("id") id: Int): Response<StaffMemberFullModel> {
        var retryCount = 0
        while (retryCount < 3) { // повторяем запрос не более 3 раз
            try {
                return api.getStaffFullFromId(id)
            } catch (e: Exception) {
                // обрабатываем ошибки
                when (e) {
                    is SocketTimeoutException, is HttpException -> {
                        retryCount++
                        Log.e("MalApiService", "Error occurred: ${e.message}")
                        delay(10000) // задержка на 10 секунд перед повторным запросом
                    }

                    else -> throw e // выбрасываем ошибку, которую не умеем обрабатывать
                }
            }
        }
        throw Exception("Failed to get response after $retryCount retries")
    }

    companion object {

        private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private val httpClient = OkHttpClient.Builder()

            .connectTimeout(40, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging).build()


        val api: MalApiService by lazy {

            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build().create(MalApiService::class.java)
        }
    }

}
