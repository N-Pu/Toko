package com.project.toko.core.repository


import android.util.Log
import com.project.toko.characterDetailedScreen.model.characterFullModel.CharacterFullModel
import com.project.toko.characterDetailedScreen.model.characterPictures.CharacterPicturesModel
import com.project.toko.detailScreen.model.detailModel.DetailScreenModel
import com.project.toko.randomAnimeScreen.model.AnimeRandomModel
import com.project.toko.detailScreen.model.castModel.CastModel
import com.project.toko.detailScreen.model.pictureModel.DetailPicturesDataModel
import com.project.toko.detailScreen.model.recommendationsModel.RecommendationsModel
import com.project.toko.homeScreen.model.newAnimeSearchModel.NewAnimeSearchModel
import com.project.toko.detailScreen.model.staffModel.StaffModel
import com.project.toko.personDetailedScreen.model.personFullModel.PersonFullModel
import com.project.toko.personDetailedScreen.model.personPictures.PersonPicturesModel
import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.SocketTimeoutException


interface MalApiService {
    companion object {
        const val BASE_URL = "https://api.jikan.moe/"
    }

    //added header for caching (return here afterwards)
    @GET("${BASE_URL}v4/anime?")
    suspend fun getAnimeSearchByName(
        @Header("If-None-Match") eTag: String?,
        @Query("sfw") sfw: Boolean,
        @Query("page") page: Int = 1,
        @Query("q") query: String? = null,
        @Query("type") type: String? = null,
        @Query("genres") genres: String? = null,
        @Query("min_score") min_score: String? = null,
        @Query("max_score") max_score: String? = null,
        @Query("rating") rating: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
    ): Response<NewAnimeSearchModel>

    // Enum: "airing" "upcoming" "bypopularity" "favorite"
    @GET("${BASE_URL}v4/top/anime?")
    suspend fun getTenTopAnime(
        @Query("filter") filter: String,
        @Query("limit") limit: Int,
        @Query("sfw") sfw: Boolean
    ): Response<NewAnimeSearchModel>


    @GET("${BASE_URL}v4/anime/{id}/full")
    suspend fun getDetailsFromAnime(@Path("id") id: Int): Response<DetailScreenModel>

    @GET("${BASE_URL}v4/anime/{id}/pictures")
    suspend fun getDetailScreenPictures(@Path("id") id: Int): Response<DetailPicturesDataModel>


    @GET("${BASE_URL}v4/anime/{id}/recommendations")
    suspend fun getRecommendationsFromAnime(@Path("id") id: Int): Response<RecommendationsModel>

    @GET("${BASE_URL}v4/random/anime")
    suspend fun getRandomAnime(@Query("sfw") sfw: Boolean): Response<AnimeRandomModel>

    @GET("${BASE_URL}v4/anime/{id}/characters")
    suspend fun getCharactersFromId(@Path("id") id: Int): Response<CastModel> {
        try {
            return getCharactersFromId(id)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return Response.success(null)
            }
            throw e
        }
    }

    @GET("${BASE_URL}v4/characters/{id}/full")
    suspend fun getCharacterFullFromId(@Path("id") id: Int): Response<CharacterFullModel>

    @GET("${BASE_URL}v4/characters/{id}/pictures")
    suspend fun getCharacterFullPictures(@Path("id") id: Int): Response<CharacterPicturesModel>

    @GET("${BASE_URL}v4/people/{id}/pictures")
    suspend fun getPersonFullPictures(@Path("id") id: Int): Response<PersonPicturesModel>

    @GET("${BASE_URL}v4/anime/{id}/staff")
    suspend fun getStaffFromId(@Path("id") id: Int): Response<StaffModel> {
        try {
            return getStaffFromId(id)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return Response.success(null)
            }
            throw e
        }
    }

    @GET("${BASE_URL}v4/people/{id}/full")
    suspend fun getPersonFullFromId(@Path("id") id: Int): Response<PersonFullModel> {
        var retryCount = 0
        while (retryCount < 3) { // повторяем запрос не более 3 раз
            try {
                return getPersonFullFromId(id)
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
}
