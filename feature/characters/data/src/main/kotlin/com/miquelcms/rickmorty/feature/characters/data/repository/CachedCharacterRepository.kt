package com.miquelcms.rickmorty.feature.characters.data.repository

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.core.domain.onSuccess
import com.miquelcms.rickmorty.feature.characters.data.remote.CharacterRemoteDataSource
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.repository.CharacterRepository
import java.util.concurrent.ConcurrentHashMap

internal class CachedCharacterRepository(
    private val remoteDataSource: CharacterRemoteDataSource,
) : CharacterRepository {

    private val cachedCharacters = ConcurrentHashMap<Int, Character>()

    override suspend fun getCharacters(
        filters: CharacterFilters,
        forceRefresh: Boolean,
        page: Int?,
    ): Result<CharacterPage, DataError> {
        var pageToLoad = page
        while (true) {
            val result = remoteDataSource.getCharacters(pageToLoad, filters, forceRefresh)
            val characterPage = when (result) {
                is Result.Success -> result.data
                is Result.Failure -> return result
            }
            val nextPage = characterPage.nextPage
            if (characterPage.characters.isNotEmpty() || nextPage == null) {
                characterPage.characters.forEach { cachedCharacters[it.id] = it }
                return Result.Success(characterPage)
            }
            pageToLoad = nextPage
        }
    }

    override suspend fun getCharacter(id: Int): Result<Character, DataError> {
        cachedCharacters[id]?.let { return Result.Success(it) }
        return remoteDataSource.getCharacter(id)
            .onSuccess { character -> cachedCharacters[character.id] = character }
    }
}
