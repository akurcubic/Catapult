package com.example.catlistapp.cats.repository

import com.example.catlistapp.cats.api.CatsApi
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.dao.CatDao
import com.example.catlistapp.cats.dao.CatGalleryDao
import com.example.catlistapp.cats.entities.CatGallery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class CatsRepository @Inject constructor(
    private val catDao: CatDao,
    private val catGalleryDao: CatGalleryDao,
    @Named("CatApi") private val catApi: CatsApi,
){

//    private val catsApi: CatsApi = retrofit.create(CatsApi::class.java)




//    suspend fun fetchAllCats(): List<CatApiModel> {
//        val cats = catsApi.getAllCats()
//
//        return cats
//    }
//
//    suspend fun fetchCatDetails(id: String): CatApiModel{
//
//        val cat = catsApi.getCat(id)
//
//        return cat
//    }
//
//    suspend fun getCatPhotos(catId: String) = catsApi.getCatImages(breedIds = catId)

    suspend fun fetchAllCatsFromApi() {
        catDao.insertAllCats(cats = catApi.getAllCats())
    }

    fun getAllCatsFlow(): Flow<List<CatApiModel>> = catDao.getAllCats()

    fun getCatByIdFlow(id: String): Flow<CatApiModel> = catDao.getCatById(id)

    suspend fun getAllCatsPhotosApi(id: String): List<CatGallery> {
        val images = catApi.getAllCatsPhotos(id).map { it.copy(id = id) }
        catGalleryDao.insertAllGalleryCats(cats = images)
        return images
    }

    fun getAllCatImagesByIdFlow(id: String): Flow<List<String>> = catGalleryDao.getAllImagesForId(id)

}