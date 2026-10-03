package com.miquelcms.rickmorty.feature.characters.data.remote

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage

internal class FakeCharacterRemoteDataSource : CharacterRemoteDataSource {

    data class PageRequest(
        val page: Int?,
        val filters: CharacterFilters,
        val forceRefresh: Boolean,
    )

    var firstPageResult: Result<CharacterPage, DataError.Network> =
        Result.Failure(DataError.Network.NOT_FOUND)
    val nextPageResults = mutableMapOf<Int, Result<CharacterPage, DataError.Network>>()
    val characterResults = mutableMapOf<Int, Result<Character, DataError.Network>>()
    val pageRequests = mutableListOf<PageRequest>()
    val characterRequests = mutableListOf<Int>()

    override suspend fun getCharacters(
        page: Int?,
        filters: CharacterFilters,
        forceRefresh: Boolean,
    ): Result<CharacterPage, DataError.Network> {
        pageRequests += PageRequest(page, filters, forceRefresh)
        return if (page == null) {
            firstPageResult
        } else {
            nextPageResults[page] ?: Result.Failure(DataError.Network.NOT_FOUND)
        }
    }

    override suspend fun getCharacter(id: Int): Result<Character, DataError.Network> {
        characterRequests += id
        return characterResults[id] ?: Result.Failure(DataError.Network.NOT_FOUND)
    }
}
