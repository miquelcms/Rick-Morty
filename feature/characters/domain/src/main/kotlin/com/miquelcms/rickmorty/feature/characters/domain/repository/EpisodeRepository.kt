package com.miquelcms.rickmorty.feature.characters.domain.repository

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode

interface EpisodeRepository {
    suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>, DataError>
}
