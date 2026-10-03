package com.miquelcms.rickmorty.feature.characters.ui

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.repository.CharacterRepository
import kotlinx.coroutines.CompletableDeferred

class FakeCharacterRepository : CharacterRepository {

    data class PageRequest(
        val page: Int?,
        val filters: CharacterFilters,
        val forceRefresh: Boolean,
    )

    var firstPageResult: Result<CharacterPage, DataError> =
        Result.Failure(DataError.Network.NOT_FOUND)
    val nextPageResults = mutableMapOf<Int, Result<CharacterPage, DataError>>()
    var characterResult: Result<Character, DataError> = Result.Failure(DataError.Network.NOT_FOUND)
    var pause: CompletableDeferred<Unit>? = null
    val pageRequests = mutableListOf<PageRequest>()
    val requestedCharacterIds = mutableListOf<Int>()

    override suspend fun getCharacters(
        filters: CharacterFilters,
        forceRefresh: Boolean,
        page: Int?,
    ): Result<CharacterPage, DataError> {
        pageRequests += PageRequest(page, filters, forceRefresh)
        return if (page == null) {
            firstPageResult
        } else {
            nextPageResults[page] ?: Result.Failure(DataError.Network.NOT_FOUND)
        }
    }

    override suspend fun getCharacter(id: Int): Result<Character, DataError> {
        requestedCharacterIds += id
        pause?.await()
        return characterResult
    }
}
