package com.project.toko.core.viewModel.viewModelFactory

import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.toko.core.repository.MalApiService
import com.project.toko.characterDetailedScreen.viewModel.CharacterFullByIdViewModel
import com.project.toko.core.dao.Dao
import com.project.toko.core.viewModel.daoViewModel.DaoViewModel
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.randomAnimeScreen.viewModel.RandomAnimeViewModel
import com.project.toko.staffDetailedScreen.viewModel.PersonByIdViewModel
import com.project.toko.producerDetailedScreen.viewModel.ProducerFullViewModel

@Suppress("UNCHECKED_CAST")
class MyViewModelFactory(
    private val malApiRepository: MalApiService,
    private val dao: Dao
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(DetailScreenViewModel::class.java) -> DetailScreenViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(PersonByIdViewModel::class.java) -> PersonByIdViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(CharacterFullByIdViewModel::class.java) -> CharacterFullByIdViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(RandomAnimeViewModel::class.java) -> RandomAnimeViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(ProducerFullViewModel::class.java) -> ProducerFullViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(DaoViewModel::class.java) -> DaoViewModel(dao) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}