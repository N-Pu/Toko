package com.project.toko.homeScreen.model.linkChangerModel


data class OrderBy(val orderBy: String, var isOrdered: Boolean = false)

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
