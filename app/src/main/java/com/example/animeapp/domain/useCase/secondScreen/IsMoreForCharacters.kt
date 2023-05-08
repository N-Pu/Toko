package com.example.animeapp.domain.useCase.secondScreen

import com.example.animeapp.domain.castModel.Data
class IsMoreForCharacters {
    fun isMoreThenTenCharacters(castList: List<Data>): List<Data> {
        return if (castList.size >= 10) {
            castList.dropLast(castList.size - 10)
        } else {
            castList
        }
    }
}
