package com.example.animeapp.domain.useCase.firstScreen



class IsMoreThenForStaff {

    fun isMoreThenSevenStaff(staffList: List<com.example.animeapp.domain.staffModel.Data>): List<com.example.animeapp.domain.staffModel.Data> {
        return if (staffList.size >= 7) {
            staffList.dropLast(staffList.size - 7)
        } else {
            staffList
        }
    }


}