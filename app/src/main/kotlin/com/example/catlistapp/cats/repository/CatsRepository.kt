package com.example.catlistapp.cats.repository

import com.example.catlistapp.cats.api.CatsApi
import com.example.catlistapp.cats.api.ResultsApi
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.dao.CatDao
import com.example.catlistapp.cats.dao.CatGalleryDao
import com.example.catlistapp.cats.entities.CatGallery
import com.example.catlistapp.leaderboard.response.LeaderboardResponse
import com.example.catlistapp.mappers.asPhotoDbModel
import com.example.catlistapp.networking.dto.ResultDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class CatsRepository @Inject constructor(
    private val catDao: CatDao,
    private val catGalleryDao: CatGalleryDao,
    @Named("CatApi") private val catApi: CatsApi,
    @Named("ResultApi") private val resultsApi: ResultsApi
){

    suspend fun fetchAllCatsFromApi() {
        catDao.insertAllCats(cats = catApi.getAllCats())
    }

    fun getAllCatsFlow(): Flow<List<CatApiModel>> = catDao.getAllCats()

    fun getCatByIdFlow(id: String): Flow<CatApiModel> = catDao.getCatById(id)

    suspend fun getAllCatPhotosApi(id: String): List<CatGallery> {
        val images = catApi.getAllCatPhotos(id).map { it.copy(id = id) }
        catGalleryDao.insertAllGalleryCats(cats = images)
        return images
    }

    suspend fun fetchAllResultsForCategory(category: Int): List<ResultDTO> {
        return resultsApi.getAllResultsForCategory(category)
    }

    suspend fun postResult(nickname: String, result:Float, category: Int): LeaderboardResponse{
        val dto = ResultDTO(nickname,result,category)
        return resultsApi.postResult(dto)
    }

    suspend fun getAllCats() = catDao.getAll()

    suspend fun getAllPhotos() = catGalleryDao.getAll()

    suspend fun fetchPhoto(photoId: String, catId: String) {
        val photo = catApi.fetchPhotoById(photoId = photoId).asPhotoDbModel(catId)
        catGalleryDao.insert(photo)
    }
   fun getPhotosByCatId(catId: String) = catGalleryDao.getPhotosByCatId(catId = catId)

}