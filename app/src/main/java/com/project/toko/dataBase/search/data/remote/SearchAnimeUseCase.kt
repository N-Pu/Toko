package com.project.toko.dataBase.search.data.remote



data class FilterParams(
    val sfw: Boolean? = null,
    val query: String? = null,
    val type: String? = null,
    val genres: String? = null,
    val min_score: String? = null,
    val max_score: String? = null,
    val rating: String? = null,
    val orderBy: String? = null,
    val sort: String? = null,
) {
    fun hasActiveFilters(): Boolean {
        return !query.isNullOrBlank() ||
                !genres.isNullOrBlank() ||
                !type.isNullOrBlank() ||
                !min_score.isNullOrBlank() ||
                !max_score.isNullOrBlank() ||
                !rating.isNullOrBlank() ||
                !orderBy.isNullOrBlank() ||
                !sort.isNullOrBlank()
    }



    override fun toString(): String {
        return "FilterParams(" +
                "sfw=$sfw," +
                "query=$query," +
                "type=$type," +
                "genres=$genres," +
                "min_score=$min_score," +
                "max_score=$max_score," +
                "rating=$rating," +
                "orderBy=$orderBy," +
                "sort=$sort)"
    }
}
