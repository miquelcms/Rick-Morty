package com.miquelcms.rickmorty.core.testing

import kotlinx.serialization.json.Json
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private val json = Json { ignoreUnknownKeys = true }

fun MockWebServer.createRetrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl(url("/"))
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
