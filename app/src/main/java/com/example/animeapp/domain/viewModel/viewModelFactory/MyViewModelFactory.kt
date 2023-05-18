package com.example.animeapp.domain.viewModel.viewModelFactory

import HomeScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animeapp.repository.MalApiService
import com.example.animeapp.domain.viewModel.CastInDetailScreenViewModel
import com.example.animeapp.domain.viewModel.CharacterFullByIdViewModel
import com.example.animeapp.domain.viewModel.CharacterPicturesViewModel
import com.example.animeapp.domain.viewModel.DetailScreenViewModel
import com.example.animeapp.domain.viewModel.IdViewModel
import com.example.animeapp.domain.viewModel.StaffFullByIdViewModel
import com.example.animeapp.domain.viewModel.StaffInDetailScreenViewModel




@Suppress("UNCHECKED_CAST")
class MyViewModelFactory(private val malApiRepository: MalApiService.Companion) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(DetailScreenViewModel::class.java) -> DetailScreenViewModel(malApiRepository.api) as T
            modelClass.isAssignableFrom(CastInDetailScreenViewModel::class.java) -> CastInDetailScreenViewModel(malApiRepository.api) as T
            modelClass.isAssignableFrom(StaffInDetailScreenViewModel::class.java) -> StaffInDetailScreenViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(StaffFullByIdViewModel::class.java) -> StaffFullByIdViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(CharacterFullByIdViewModel::class.java) -> CharacterFullByIdViewModel(malApiRepository.api) as T
            modelClass.isAssignableFrom(CharacterPicturesViewModel::class.java) -> CharacterPicturesViewModel(malApiRepository.api) as T
            modelClass.isAssignableFrom(IdViewModel::class.java) -> IdViewModel() as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}