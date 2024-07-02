package com.example.catlistapp.cats.details

import com.example.catlistapp.cats.details.model.CatDetailsUiModel

interface CatDetailsContract {

    data class CatDetailsState(

        val loading: Boolean = false,
        val cat: CatDetailsUiModel? = null,
    )
}