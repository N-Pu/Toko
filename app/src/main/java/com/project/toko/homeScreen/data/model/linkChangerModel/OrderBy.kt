package com.project.toko.homeScreen.data.model.linkChangerModel


data class OrderBy(val name: String)

fun getOrderBy() =
    listOf(
        OrderBy("mal_id"),
        OrderBy("title"),
        OrderBy("start_date"),
        OrderBy("end_date"),
        OrderBy("episodes"),
        OrderBy("score"),
        OrderBy("scored_by"),
        OrderBy("rank"),
        OrderBy("popularity"),
        OrderBy("members"),
    )
