package com.example.animeapp.repository


import android.util.Log
import com.example.animeapp.domain.models.castModel.CastModel
import com.example.animeapp.domain.models.characterModel.CharacterFullModel
import com.example.animeapp.domain.models.characterPictures.CharacterPicturesModel
import com.example.animeapp.domain.models.detailModel.AnimeDetailModel
import com.example.animeapp.domain.models.newAnimeSearchModel.NewAnimeSearchModel
import com.example.animeapp.domain.models.personFullModel.PersonFullModel
import com.example.animeapp.domain.models.producerModel.ProducerFullModel
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


    @GET("${BASE_URL}v4/anime?")
    suspend fun getAnimeSearchByName(
        @Query("sfw") sfw: Boolean,
        @Query("page") page: Int = 1,
        @Query("q") nameOfAnime: String,
        @Query("type") type: String? = null,
        @Query("genres") genres: String = "",
        @Query("min_score") min_score: String? = null,
        @Query("max_score") max_score: String? = null,
        @Query("rating") rating: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
//        @Query("start_date") start_date: String? = null,
//        @Query("end_date") end_date: String? = null,
    ): Response<NewAnimeSearchModel> {
        val ratingParam = if (rating != null) "rating=$rating" else null
        val orderByParam = if (orderBy != null) "orderBy=$orderBy" else null
        val typeParam = if (type != null) "type=$type" else null
        val sortParam = if (sort != null) "sort=$sort" else null
        val minParam = if (min_score != null) "minParam=$min_score" else null
        val maxParam = if (max_score != null) "maxParam=$max_score" else null
//        val startDateParam = if (start_date != null) "start_date=$start_date" else null
//        val endDateParam = if (end_date != null) "end_date=$end_date" else null
        return try {
            api.getAnimeSearchByName(
                sfw,
                page,
                nameOfAnime,
                typeParam,
                genres,
                minParam,
                maxParam,
                ratingParam,
                orderByParam,
                sortParam,
//                startDateParam,
//                endDateParam
            )
        } catch (e: HttpException) {
            // Handle exceptions
            throw e
        }
    }

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
    suspend fun getPersonFullFromId(@Path("id") id: Int): Response<PersonFullModel> {
        var retryCount = 0
        while (retryCount < 3) { // повторяем запрос не более 3 раз
            try {
                return api.getPersonFullFromId(id)
            } catch (e: Exception) {
                // обрабатываем ошибки
                when (e) {
                    is SocketTimeoutException, is HttpException -> {
                        retryCount++
                        Log.e("MalApiService", "Error occurred: ${e.message}")
                        delay(1000) // задержка на 1 секунд перед повторным запросом
                    }

                    else -> throw e // выбрасываем ошибку, которую не умеем обрабатывать
                }
            }
        }
        throw Exception("Failed to get response after $retryCount retries")
    }


    @GET("${BASE_URL_FOR_CHARACTER}producers/{id}/full")
    suspend fun getProducerFullFromId(@Path("id") id: Int): Response<ProducerFullModel> {
        var retryCount = 0
        while (retryCount < 3) { // повторяем запрос не более 3 раз
            try {
                return api.getProducerFullFromId(id)
            } catch (e: Exception) {
                // обрабатываем ошибки
                when (e) {
                    is SocketTimeoutException, is HttpException -> {
                        retryCount++
                        Log.e("MalApiService", "Error occurred: ${e.message}")
                        delay(1000) // задержка на 1 секунд перед повторным запросом
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

            .connectTimeout(20, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging).build()


        val api: MalApiService by lazy {

            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build().create(MalApiService::class.java)
        }
    }

}
