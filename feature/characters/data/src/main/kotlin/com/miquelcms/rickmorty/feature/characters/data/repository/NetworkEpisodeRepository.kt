package com.miquelcms.rickmorty.feature.characters.data.repository

import com.miquelcms.rickmorty.core.analytics.ErrorReporter
import com.miquelcms.rickmorty.core.data.network.safeCall
import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.core.domain.map
import com.miquelcms.rickmorty.feature.characters.data.mapper.toEpisode
import com.miquelcms.rickmorty.feature.characters.data.remote.EpisodeApi
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode
import com.miquelcms.rickmorty.feature.characters.domain.repository.EpisodeRepository

internal class NetworkEpisodeRepository(
    private val api: EpisodeApi,
    private val errorReporter: ErrorReporter,
) : EpisodeRepository {

    override suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>, DataError> {
        if (ids.isEmpty()) return Result.Success(emptyList())
        val idsPath = ids.joinToString(separator = ",", prefix = "[", postfix = "]")
        return safeCall(errorReporter) { api.getEpisodes(idsPath) }
            .map { episodes -> episodes.map { it.toEpisode() } }
    }
}
