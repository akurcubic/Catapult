package com.example.catlistapp.cats.gallery

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.gallery.photo.catId
import com.example.catlistapp.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject

@HiltViewModel
class CatGalleryViewModel @Inject constructor(

    savedStateHandle: SavedStateHandle,
    private val catsRepository: CatsRepository
): ViewModel() {

    private val catId: String = savedStateHandle.catId
    private val _state = MutableStateFlow(CatGalleryContract.CatGalleryState(catId = catId))
    val state = _state.asStateFlow()
    private fun setState(reducer: CatGalleryContract.CatGalleryState.() -> CatGalleryContract.CatGalleryState) =
        _state.update(reducer)

    init {

        fetchCatGallery()
        observePhotos()

    }

    private fun fetchCatGallery() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    catsRepository.getAllCatPhotosApi(id = catId)
                    Log.e("FETCH", "Fetch Photos")
                }
            } catch (error: Exception) {
                Log.d("FETCH", "Fetch Photos Error", error)
            }
            setState { copy(loading = false) }
        }
    }

    private fun observePhotos() {
        viewModelScope.launch {
            catsRepository.getPhotosByCatId(catId = catId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(photos = it) }
                }
        }
    }

}
