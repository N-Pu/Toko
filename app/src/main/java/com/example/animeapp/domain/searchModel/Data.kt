package com.example.animeapp.domain.searchModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(

    
    @SerializedName("episodes") val episodes: Int,

    
    @SerializedName("title") val title: String,

    
    @SerializedName("images") val images: Images,

    
    @SerializedName("score") val score: Float,

    
    @SerializedName("scored_by") val scored_by: Float,

    
    @SerializedName("mal_id") val mal_id: Int


)