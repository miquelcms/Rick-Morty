package com.miquelcms.rickmorty.feature.characters.domain

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage

interface CharacterRepository {
    suspend fun getCharacters(page: Int, filters: CharacterFilters): Result<CharacterPage, DataError>

    suspend fun getCharacter(id: Int): Result<Character, DataError>
}
