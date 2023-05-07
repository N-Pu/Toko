package com.example.animeapp.domain.repository



import com.example.animeapp.domain.castModel.CastModel
import com.example.animeapp.domain.characterModel.CharacterFullModel
import com.example.animeapp.domain.characterPictures.CharacterPicturesModel
import com.example.animeapp.domain.detailModel.AnimeDetailModel
import com.example.animeapp.domain.searchModel.AnimeSearchModel
import com.example.animeapp.domain.staffMemberFullModel.StaffMemberFullModel
import com.example.animeapp.domain.staffModel.StaffModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.jikan.moe/"
private const val BASE_URL_FOR_CHARACTER = "https://api.jikan.moe/v4/"
private const val GET_DETAILS_URL = "https://api.jikan.moe/v4/anime/"


interface MalApiService {


    @GET("${BASE_URL}v4/anime")    // (в конце добавить лимит limit=20)
    suspend fun getAnimeSearchByName(@Query("q") nameOfAnime: String): Response<AnimeSearchModel>

    @GET("$GET_DETAILS_URL{id}/full")
    suspend fun getDetailsFromAnime(@Path("id") id: Int): Response<AnimeDetailModel>

    @GET("$GET_DETAILS_URL{id}/characters")
    suspend fun getCharactersFromId(@Path("id") id: Int): Response<CastModel>

    @GET("${BASE_URL_FOR_CHARACTER}characters/{id}/full")
    suspend fun getCharacterFullFromId(@Path("id") id: Int): Response<CharacterFullModel>
    @GET("${BASE_URL_FOR_CHARACTER}characters/{id}/pictures")
    suspend fun getCharacterFullPictures(@Path("id") id: Int): Response<CharacterPicturesModel>

    @GET("$GET_DETAILS_URL{id}/staff")
    suspend fun getStaffFromId(@Path("id") id: Int): Response<StaffModel>


    @GET("${BASE_URL_FOR_CHARACTER}people/{id}/full")
    suspend fun getStaffFullFromId(@Path("id") id: Int): Response<StaffMemberFullModel>

    companion object {

        private val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)


        private val httpClient = OkHttpClient
            .Builder()
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
