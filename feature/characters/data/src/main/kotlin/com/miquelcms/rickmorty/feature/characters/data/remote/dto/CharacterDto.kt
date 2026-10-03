package com.miquelcms.rickmorty.feature.characters.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("status") val status: String,
    @SerialName("species") val species: String,
    @SerialName("type") val type: String,
    @SerialName("gender") val gender: String,
    @SerialName("origin") val origin: LocationDto,
    @SerialName("location") val location: LocationDto,
    @SerialName("image") val image: String,
    @SerialName("episode") val episode: List<String>,
)

@Serializable
internal data class LocationDto(
    @SerialName("name") val name: String,
)
