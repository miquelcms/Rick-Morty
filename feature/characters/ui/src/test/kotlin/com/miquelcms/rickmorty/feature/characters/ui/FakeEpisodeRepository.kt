package com.miquelcms.rickmorty.feature.characters.ui

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode
import com.miquelcms.rickmorty.feature.characters.domain.repository.EpisodeRepository
import kotlinx.coroutines.CompletableDeferred

class FakeEpisodeRepository : EpisodeRepository {

    var episodesResult: Result<List<Episode>, DataError> = Result.Success(emptyList())
    var pause: CompletableDeferred<Unit>? = null
    val requestedIds = mutableListOf<List<Int>>()

    override suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>, DataError> {
        requestedIds += ids
        pause?.await()
        return episodesResult
    }
}
