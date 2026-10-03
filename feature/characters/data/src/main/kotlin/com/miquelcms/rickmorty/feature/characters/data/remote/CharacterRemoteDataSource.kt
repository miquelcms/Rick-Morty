package com.miquelcms.rickmorty.feature.characters.data.remote

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage

internal interface CharacterRemoteDataSource {
    suspend fun getCharacters(
        page: Int?,
        filters: CharacterFilters,
        forceRefresh: Boolean,
    ): Result<CharacterPage, DataError.Network>

    suspend fun getCharacter(id: Int): Result<Character, DataError.Network>
}
