package com.example.catlistapp.cats.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CatApiGalleryModel(

    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)

//{"id":"ZocD-pQxd","url":"https://cdn2.thecatapi.com/images/ZocD-pQxd.jpg","width":880,"height":1100}

//{
//    "id": "J2PmlIizw",
//    "url": "https://cdn2.thecatapi.com/images/J2PmlIizw.jpg",
//    "width": 1080,
//    "height": 1350
//}