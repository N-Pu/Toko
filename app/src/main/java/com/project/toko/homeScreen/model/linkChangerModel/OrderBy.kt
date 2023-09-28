package com.project.toko.homeScreen.model.linkChangerModel


data class OrderBy(val orderBy: String, var isOrdered: Boolean = false)

fun getOrderBy(): List<OrderBy> =
     listOf(
        OrderBy("mal_id"),
        OrderBy("title"),
        OrderBy("type"),
        OrderBy("start_date"),
        OrderBy("end_date"),
        OrderBy("episodes"),
        OrderBy("score"),
        OrderBy("scored_by"),
        OrderBy("rank"),
        OrderBy("popularity"),
        OrderBy("members"),
    )