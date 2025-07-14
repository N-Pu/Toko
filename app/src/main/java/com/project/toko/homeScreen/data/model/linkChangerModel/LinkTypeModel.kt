package com.project.toko.homeScreen.data.model.linkChangerModel

import com.project.toko.homeScreen.ui.homeScreen.FilterItem


data class Types(override val name: String): FilterItem

fun getTypes(): List<Types> {
    return listOf(
        Types("tv"),
        Types("movie"),
        Types("ova"),
        Types("special"),
        Types("ona"),
        Types("music")
    )
}