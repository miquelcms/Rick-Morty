package com.miquelcms.rickmorty.feature.characters.data

import kotlinx.serialization.json.Json
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private val json = Json { ignoreUnknownKeys = true }

internal fun MockWebServer.createRetrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl(url("/"))
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
