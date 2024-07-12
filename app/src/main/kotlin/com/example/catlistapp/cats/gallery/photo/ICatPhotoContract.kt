package com.example.catlistapp.cats.gallery.photo

import com.example.catlistapp.cats.entities.CatGallery

interface ICatPhotoContract {

    data class CatPhotoState(

        val isLoading: Boolean = false,
        val photos: List<CatGallery> = emptyList(),
        val photoIndex: Int = 0,
        val error: DetailsError? = null
    ) {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
}