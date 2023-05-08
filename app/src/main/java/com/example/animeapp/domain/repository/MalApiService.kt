package com.example.animeapp.domain.repository


import android.util.Log
import com.example.animeapp.domain.castModel.CastModel
import com.example.animeapp.domain.characterModel.CharacterFullModel
import com.example.animeapp.domain.characterPictures.CharacterPicturesModel
import com.example.animeapp.domain.detailModel.AnimeDetailModel
import com.example.animeapp.domain.searchModel.AnimeSearchModel
import com.example.animeapp.domain.staffMemberFullModel.StaffMemberFullModel
import com.example.animeapp.domain.staffModel.StaffModel
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


    @GET("${BASE_URL}v4/anime")    // (в конце добавить лимит limit=20)
    suspend fun getAnimeSearchByName(@Query("q") nameOfAnime: String): Response<AnimeSearchModel>

    @GET("$GET_DETAILS_URL{id}/full")
    suspend fun getDetailsFromAnime(@Path("id") id: Int): Response<AnimeDetailModel>

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
                    is SocketTimeoutException,
                    is HttpException -> {
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

        private val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)


        private val httpClient = OkHttpClient
            .Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()


        val api: MalApiService by lazy {

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
                .create(MalApiService::class.java)
        }
    }


}
