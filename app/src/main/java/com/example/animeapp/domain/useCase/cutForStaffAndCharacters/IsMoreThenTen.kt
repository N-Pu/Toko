package com.example.animeapp.domain.useCase.cutForStaffAndCharacters



// If we receive data with more then 10 characters
// we get the first 10 of them and show in detail screen
fun <T>isMoreThenTen(staffList: List<T>): List<T> {
        return if (staffList.size >= 10) {
            staffList.dropLast(staffList.size - 10)
        } else {
            staffList
        }
    }


//fun <T>isMoreThenTen(staffList: List<T>): Boolean {
//    return staffList.size >= 10
//}