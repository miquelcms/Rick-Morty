package com.miquelcms.rickmorty.feature.characters.ui

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.repository.CharacterRepository

class FakeCharacterRepository : CharacterRepository {

    data class PageRequest(
        val page: Int,
        val filters: CharacterFilters,
        val forceRefresh: Boolean,
    )

    val pageResults = mutableMapOf<Int, Result<CharacterPage, DataError>>()
    val pageRequests = mutableListOf<PageRequest>()

    override suspend fun getCharacters(
        page: Int,
        filters: CharacterFilters,
        forceRefresh: Boolean,
    ): Result<CharacterPage, DataError> {
        pageRequests += PageRequest(page, filters, forceRefresh)
        return pageResults[page] ?: Result.Failure(DataError.Network.NOT_FOUND)
    }

    override suspend fun getCharacter(id: Int): Result<Character, DataError> =
        error("Not used in these tests")
}
