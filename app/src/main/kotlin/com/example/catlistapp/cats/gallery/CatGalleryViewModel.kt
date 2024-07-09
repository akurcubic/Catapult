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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
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

        observeCatGallery()

    }

    private fun observeCatGallery() {

        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                var newPhotos = catsRepository.getAllCatImagesByIdFlow(id = catId).first()
                Log.d("Gallerija", "Slike za macku1 = $newPhotos")
                if(newPhotos.size == 1) {
                    withContext(Dispatchers.IO) {
                        catsRepository.getAllCatsPhotosApi(id = catId)
                        newPhotos = catsRepository.getAllCatImagesByIdFlow(id = catId).first()
                        Log.d("Gallerija", "Slike za macku2 = $newPhotos")
                    }
                }
                else{
                    newPhotos = catsRepository.getAllCatImagesByIdFlow(id = catId).first()
                    Log.d("Gallerija", "Slike za macku3 = $newPhotos")
                }
                Log.d("Gallerija", "Slike za macku konacno = $newPhotos")
                setState { copy(photos = newPhotos, loading = false) }

            }catch (error: IOException){
                println(error)
            }finally {
                setState { copy(photos = photos, loading = false) }
            }
        }
    }
}
