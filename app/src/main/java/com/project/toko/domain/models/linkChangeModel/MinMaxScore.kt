package com.project.toko.domain.models.linkChangeModel


data class MinMaxScore(
    val ratingState: Int,
    val minScore: String?,
    val maxScore: String?,

    )


private fun getMinMaxScoreList(): List<MinMaxScore> {
    return listOf(
        MinMaxScore(ratingState = 0, minScore = null, maxScore = null),
        MinMaxScore(ratingState = 1, minScore = "0.001", maxScore = "1.999"),
        MinMaxScore(ratingState = 2, minScore = "2.000", maxScore = "2.999"),
        MinMaxScore(ratingState = 3, minScore = "3.000", maxScore = "3.999"),
        MinMaxScore(ratingState = 4, minScore = "3.999", maxScore = "4.999"),
        MinMaxScore(ratingState = 5, minScore = "4.999", maxScore = "5.999"),
        MinMaxScore(ratingState = 6, minScore = "5.999", maxScore = "6.999"),
        MinMaxScore(ratingState = 7, minScore = "6.999", maxScore = "7.999"),
        MinMaxScore(ratingState = 8, minScore = "7.999", maxScore = "8.999"),
        MinMaxScore(ratingState = 9, minScore = "8.999", maxScore = "9.999"),
        MinMaxScore(ratingState = 10, minScore = "9.000", maxScore = "10.000")
    )
}


data class Score(var score: String?)

fun getStateScore(scoreNumber: Int): MinMaxScore {
    return getMinMaxScoreList()[scoreNumber]
}