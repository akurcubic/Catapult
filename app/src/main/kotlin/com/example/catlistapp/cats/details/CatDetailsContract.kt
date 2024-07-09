package com.example.catlistapp.cats.details

import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.details.model.CatDetailsUiModel
import com.example.catlistapp.cats.gallery.model.CatGalleryUiModel

interface CatDetailsContract {

    data class CatDetailsState(

        val catId: String,
        val loading: Boolean = false,
        val cat: CatApiModel? = null,
        val photo: CatGalleryUiModel? = null,
    )
}