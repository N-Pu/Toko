package com.example.animeapp.domain.useCase.secondScreen

import com.example.animeapp.domain.castModel.Data
class IsMoreForCharacters {
    fun isMoreThenSevenCharacters(castList: List<Data>): List<Data> {
        return if (castList.size >= 7) {
            castList.dropLast(castList.size - 7)
        } else {
            castList
        }
    }
}
