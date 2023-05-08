package com.example.animeapp.domain.useCase.firstScreen


class IsMoreThenForStaff {

    fun isMoreThenTenStaff(staffList: List<com.example.animeapp.domain.staffModel.Data>): List<com.example.animeapp.domain.staffModel.Data> {
        return if (staffList.size >= 10) {
            staffList.dropLast(staffList.size - 10)
        } else {
            staffList
        }
    }


}