package com.miquelcms.rickmorty.feature.characters.data.mapper

import com.miquelcms.rickmorty.feature.characters.data.remote.dto.EpisodeDto
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode

internal fun EpisodeDto.toEpisode() = Episode(
    id = id,
    name = name,
    code = episode,
)
