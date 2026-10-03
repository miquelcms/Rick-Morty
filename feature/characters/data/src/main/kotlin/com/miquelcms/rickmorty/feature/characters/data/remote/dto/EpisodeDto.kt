package com.miquelcms.rickmorty.feature.characters.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EpisodeDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("episode") val episode: String,
)
