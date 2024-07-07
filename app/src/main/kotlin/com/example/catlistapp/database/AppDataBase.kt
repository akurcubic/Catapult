package com.example.catlistapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.dao.CatDao
import com.example.catlistapp.cats.dao.CatGalleryDao
import com.example.catlistapp.cats.entities.CatGallery

@Database(
    entities = [
        CatApiModel::class, CatGallery::class
    ],
    version = 1,
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ]
)

abstract class AppDataBase: RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun catGalleryDao(): CatGalleryDao
}