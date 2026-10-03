package com.miquelcms.rickmorty.feature.characters.domain

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode

class FakeEpisodeRepository : EpisodeRepository {

    var episodesResult: Result<List<Episode>, DataError> = Result.Success(emptyList())
    val requestedIds = mutableListOf<List<Int>>()

    override suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>, DataError> {
        requestedIds += ids
        return episodesResult
    }
}
