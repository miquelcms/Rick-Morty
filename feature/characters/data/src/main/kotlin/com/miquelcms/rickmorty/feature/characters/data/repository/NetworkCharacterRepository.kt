package com.miquelcms.rickmorty.feature.characters.data.repository

import com.miquelcms.rickmorty.core.analytics.ErrorReporter
import com.miquelcms.rickmorty.core.data.network.safeCall
import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.core.domain.map
import com.miquelcms.rickmorty.feature.characters.data.mapper.toCharacter
import com.miquelcms.rickmorty.feature.characters.data.mapper.toCharacterPage
import com.miquelcms.rickmorty.feature.characters.data.mapper.toQueryValue
import com.miquelcms.rickmorty.feature.characters.data.remote.api.CharacterApi
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.repository.CharacterRepository
import java.util.concurrent.ConcurrentHashMap

internal class NetworkCharacterRepository(
    private val api: CharacterApi,
    private val errorReporter: ErrorReporter,
) : CharacterRepository {

    private val cachedCharacters = ConcurrentHashMap<Int, Character>()

    override suspend fun getCharacters(
        page: Int,
        filters: CharacterFilters,
        forceRefresh: Boolean,
    ): Result<CharacterPage, DataError> {
        val species = filters.species?.toQueryValue()
        val result = safeCall(errorReporter) {
            api.getCharacters(
                page = page,
                name = filters.name.ifBlank { null },
                status = filters.status?.toQueryValue(),
                species = species,
                gender = filters.gender?.toQueryValue(),
                cacheControl = if (forceRefresh) NO_CACHE else null,
            )
        }
        return when (result) {
            is Result.Success -> {
                val characterPage = result.data.toCharacterPage().keepOnlySpecies(species)
                characterPage.characters.forEach { cachedCharacters[it.id] = it }
                Result.Success(characterPage)
            }
            is Result.Failure -> when (result.error) {
                DataError.Network.NOT_FOUND -> Result.Success(EMPTY_PAGE)
                else -> result
            }
        }
    }

    override suspend fun getCharacter(id: Int): Result<Character, DataError> {
        cachedCharacters[id]?.let { return Result.Success(it) }
        return safeCall(errorReporter) { api.getCharacter(id) }
            .map { dto -> dto.toCharacter().also { cachedCharacters[it.id] = it } }
    }

    private fun CharacterPage.keepOnlySpecies(species: String?): CharacterPage =
        if (species == null) {
            this
        } else {
            copy(characters = characters.filter { it.species.equals(species, ignoreCase = true) })
        }

    private companion object {
        const val NO_CACHE = "no-cache"
        val EMPTY_PAGE = CharacterPage(characters = emptyList(), hasNextPage = false)
    }
}
