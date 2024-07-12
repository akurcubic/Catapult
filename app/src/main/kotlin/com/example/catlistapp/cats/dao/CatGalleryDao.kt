package com.example.catlistapp.cats.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catlistapp.cats.entities.CatGallery
import kotlinx.coroutines.flow.Flow


@Dao
interface CatGalleryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGalleryCats(cats: List<CatGallery>)

    @Query("SELECT * FROM images")
    suspend fun getAll(): List<CatGallery>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: CatGallery)

    @Query("SELECT * FROM images WHERE id = :catId")
    fun getPhotosByCatId(catId: String): Flow<List<CatGallery>>
}