package com.example.catlistapp.cats.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.gallery.photo.catId
import com.example.catlistapp.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatDetailsViewModel @Inject constructor(

    savedStateHandle: SavedStateHandle,
    private val catsRepository: CatsRepository
): ViewModel(){

    private val catId: String = savedStateHandle.catId
    private val _state = MutableStateFlow(CatDetailsContract.CatDetailsState(catId = catId))
    val state = _state.asStateFlow()
    private fun setState(reducer: CatDetailsContract.CatDetailsState.() -> CatDetailsContract.CatDetailsState) = _state.update(reducer)

    init{

        observeCatDetails()
    }


    private fun observeCatDetails() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            catsRepository.getCatByIdFlow(id = catId)
                .distinctUntilChanged()
                .collect { catInfoDetail ->
                setState { copy(cat = catInfoDetail, loading = false) }

            }
        }
    }
}