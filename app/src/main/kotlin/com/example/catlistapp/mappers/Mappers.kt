package com.example.catlistapp.mappers

import com.example.catlistapp.cats.api.model.CatApiGalleryModel
import com.example.catlistapp.cats.entities.CatGallery
import com.example.catlistapp.cats.gallery.model.CatGalleryUiModel

fun CatApiGalleryModel.asPhotoDbModel(catId: String): CatGallery {
    return CatGallery(

        url = this.url,
        id = catId
    )
}

fun CatGallery.asGalleryUiModel(): CatGalleryUiModel {
    return CatGalleryUiModel(

        id = this.id,
        url = this.url,
    )
}