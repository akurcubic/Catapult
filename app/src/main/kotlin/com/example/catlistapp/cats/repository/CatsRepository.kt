package com.example.catlistapp.cats.repository

import com.example.catlistapp.cats.api.CatsApi
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.networking.retrofit

object CatsRepository {

    private val catsApi: CatsApi = retrofit.create(CatsApi::class.java)


    suspend fun fetchAllCats(): List<CatApiModel> {
        val cats = catsApi.getAllCats()

        return cats
    }

    suspend fun fetchCatDetails(id: String): CatApiModel{

        val cat = catsApi.getCat(id)

        return cat
    }
}