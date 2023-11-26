package com.project.toko.homeScreen.model.linkChangerModel


data class MinMaxScore(
    val number: Int,
    val minScore: String?,
    val maxScore: String?
)

data class Score(var score: String?)

fun getMinMaxScore(scoreNumber: Int): MinMaxScore  {

    val list = listOf(
        MinMaxScore(
            number = 0,
            minScore = null,
            maxScore = null
        ),
        MinMaxScore(
            number = 1,
            minScore = "0.001",
            maxScore = "1.999"
        ),
        MinMaxScore(
            number = 2,
            minScore = "2.000",
            maxScore = "2.999"
        ),
        MinMaxScore(
            number = 3,
            minScore = "3.000",
            maxScore = "3.999"
        ),
        MinMaxScore(
            number = 4,
            minScore = "3.999",
            maxScore = "4.999"
        ),
        MinMaxScore(
            number = 5,
            minScore = "4.999",
            maxScore = "5.999"
        ),
        MinMaxScore(
            number = 6,
            minScore = "5.999",
            maxScore = "6.999"
        ),
        MinMaxScore(
            number = 7,
            minScore = "6.999",
            maxScore = "7.999"
        ),
        MinMaxScore(
            number = 8,
            minScore = "7.999",
            maxScore = "8.999"
        ),
        MinMaxScore(
            number = 9,
            minScore = "8.999",
            maxScore = "9.999"
        ),
        MinMaxScore(
            number = 10,
            minScore = "9.000",
            maxScore = "10.000"
        )
    )
    return list[scoreNumber]
}