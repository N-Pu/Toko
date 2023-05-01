package com.example.animeapp.viewModel.viewModelFactory

import HomeScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animeapp.domain.repository.MalApiService
import com.example.animeapp.viewModel.CharactersViewModel
import com.example.animeapp.viewModel.DetailScreenViewModel
import com.example.animeapp.viewModel.IdViewModel
import com.example.animeapp.viewModel.StaffViewModel


//private val malApiRepository = MalApiService.api

@Suppress("UNCHECKED_CAST")
class MyViewModelFactory(private val malApiRepository: MalApiService.Companion) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(malApiRepository) as T

            modelClass.isAssignableFrom(DetailScreenViewModel::class.java) -> DetailScreenViewModel(malApiRepository) as T

            modelClass.isAssignableFrom(CharactersViewModel::class.java) -> CharactersViewModel(malApiRepository) as T

            modelClass.isAssignableFrom(StaffViewModel::class.java) -> StaffViewModel(malApiRepository) as T

            modelClass.isAssignableFrom(IdViewModel::class.java) -> IdViewModel() as T


            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}