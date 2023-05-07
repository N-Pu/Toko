package com.example.animeapp.viewModel.viewModelFactory

import HomeScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animeapp.domain.repository.MalApiService
import com.example.animeapp.viewModel.CastInDetailScreenViewModel
import com.example.animeapp.viewModel.CharacterFullByIdViewModel
import com.example.animeapp.viewModel.CharacterPicturesViewModel
import com.example.animeapp.viewModel.DetailScreenViewModel
import com.example.animeapp.viewModel.IdViewModel
import com.example.animeapp.viewModel.StaffFullByIdViewModel
import com.example.animeapp.viewModel.StaffInDetailScreenViewModel




@Suppress("UNCHECKED_CAST")
class MyViewModelFactory(private val malApiRepository: MalApiService.Companion) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(DetailScreenViewModel::class.java) -> DetailScreenViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(CastInDetailScreenViewModel::class.java) -> CastInDetailScreenViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(StaffInDetailScreenViewModel::class.java) -> StaffInDetailScreenViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(StaffFullByIdViewModel::class.java) -> StaffFullByIdViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(CharacterFullByIdViewModel::class.java) -> CharacterFullByIdViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(CharacterPicturesViewModel::class.java) -> CharacterPicturesViewModel(malApiRepository) as T
            modelClass.isAssignableFrom(IdViewModel::class.java) -> IdViewModel() as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}