package com.miquelcms.rickmorty.feature.characters.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterPageDto(
    @SerialName("info") val info: PageInfoDto,
    @SerialName("results") val results: List<CharacterDto>,
)

@Serializable
internal data class PageInfoDto(
    @SerialName("next") val next: String?,
)
