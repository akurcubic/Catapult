package com.example.catlistapp.cats.photo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.api.model.CatApiGalleryModel
import com.example.catlistapp.cats.gallery.CatGalleryContract
import com.example.catlistapp.cats.gallery.model.CatGalleryUiModel
import com.example.catlistapp.cats.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CatPhotoViewModel (
    private val catId: String,
    private val catsRepository: CatsRepository = CatsRepository
): ViewModel(){

    private val _state = MutableStateFlow(CatGalleryContract.CatGalleryState(catId = catId))
    val state = _state.asStateFlow()
    private fun setState(reducer: CatGalleryContract.CatGalleryState.() -> CatGalleryContract.CatGalleryState) = _state.update(reducer)

    init {
        fetchCatGallery()
    }

    private fun fetchCatGallery() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val catPhotos = withContext(Dispatchers.IO) {
                    catsRepository.getCatPhotos(catId = catId).map {it.asCatGalleryUiModel()}
                }
                Log.d("CatPhotoViewModel", "Cat id: $catId")
                Log.d("CatPhotoViewModel", "Cat photos from catViewModel fetched: $catPhotos")

                setState { copy(photos = catPhotos) }
            } catch (error: IOException) {
                Log.e("CatPhotoViewModel", "Error fetching photos from catViewModel: $error")
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun CatApiGalleryModel.asCatGalleryUiModel() = CatGalleryUiModel(

        id = this.id,
        url = this.url
    )
}