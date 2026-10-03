package com.miquelcms.rickmorty.core.data.network

import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File

private const val BASE_URL = "https://rickandmortyapi.com/api/"
private const val CACHE_DIRECTORY = "http_cache"
private const val CACHE_SIZE_BYTES = 10L * 1024 * 1024

internal fun createJson(): Json = Json { ignoreUnknownKeys = true }

internal fun createOkHttpClient(cacheDir: File, loggingEnabled: Boolean): OkHttpClient =
    OkHttpClient.Builder()
        .cache(Cache(File(cacheDir, CACHE_DIRECTORY), CACHE_SIZE_BYTES))
        .apply {
            if (loggingEnabled) {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
        }
        .build()

internal fun createRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
