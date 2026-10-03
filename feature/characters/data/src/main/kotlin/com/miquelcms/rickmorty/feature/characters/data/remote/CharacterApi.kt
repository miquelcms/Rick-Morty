package com.miquelcms.rickmorty.feature.characters.data.remote

import com.miquelcms.rickmorty.feature.characters.data.remote.dto.CharacterDto
import com.miquelcms.rickmorty.feature.characters.data.remote.dto.CharacterPageDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CharacterApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?,
        @Header("Cache-Control") cacheControl: String?,
    ): CharacterPageDto

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto
}
