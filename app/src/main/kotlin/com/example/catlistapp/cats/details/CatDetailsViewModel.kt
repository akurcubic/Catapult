package com.example.catlistapp.cats.details


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.details.model.CatDetailsUiModel
import com.example.catlistapp.cats.gallery.photo.catId
import com.example.catlistapp.cats.repository.CatsRepository
import com.example.catlistapp.mappers.asGalleryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

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
            catsRepository.getCatByIdFlow(id = catId).collect { catInfoDetail ->
                setState { copy(cat = catInfoDetail, loading = false) }
                catInfoDetail.reference_image_id?.let { fetchImage(it, catInfoDetail.id) }
            }
        }
    }

    private fun fetchImage(photoId: String, catId: String){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    catsRepository.fetchPhoto(photoId = photoId, catId = catId)
                }
                getImage()
            } catch (error: IOException) {
                println(error)
            }
        }
    }

    private fun getImage(){
        viewModelScope.launch {
            try {
                val photo = withContext(Dispatchers.IO) {
                    catsRepository.getPhotosByCatId(catId).last().asGalleryUiModel()
                }
                setState { copy(photo = photo) }
            } catch (error: IOException) {
                println(error)
            }
        }
    }
}