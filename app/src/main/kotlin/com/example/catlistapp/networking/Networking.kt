package com.example.catlistapp.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.example.catlistapp.networking.serialization.AppJson


//val okHttpClient = OkHttpClient.Builder()
//    .addInterceptor {
//        val updatedRequest = it.request().newBuilder()
//            .addHeader("x-api-key", "live_kIByIyZREO5nCP0vIvjkS1nLRPQl2dQT4k5XKvcEHZSBL8KjBDXsZicLZDzVHEDE")
//            .build()
//        it.proceed(updatedRequest)
//    }
//    .addInterceptor(
//        HttpLoggingInterceptor().apply {
//            setLevel(HttpLoggingInterceptor.Level.BODY)
//        }
//    )
//    .build()
//
//
//val retrofit: Retrofit = Retrofit.Builder()
//    .baseUrl("https://api.thecatapi.com/v1/")
//    .client(okHttpClient)
//    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
//    .build()
