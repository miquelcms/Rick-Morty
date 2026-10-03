package com.miquelcms.rickmorty.feature.characters.domain

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage

class FakeCharacterRepository : CharacterRepository {

    var characterResult: Result<Character, DataError> = Result.Failure(DataError.Network.NOT_FOUND)

    override suspend fun getCharacters(page: Int, filters: CharacterFilters): Result<CharacterPage, DataError> =
        error("Not used in these tests")

    override suspend fun getCharacter(id: Int): Result<Character, DataError> = characterResult
}
