package com.miquelcms.rickmorty.feature.characters.domain

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterGender
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterStatus
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCharacterEpisodesUseCaseTest {

    private val characterRepository = FakeCharacterRepository()
    private val episodeRepository = FakeEpisodeRepository()
    private val getCharacterEpisodes = GetCharacterEpisodesUseCase(characterRepository, episodeRepository)

    @Test
    fun `returns the episodes of the character`() = runTest {
        val episodes = listOf(Episode(id = 1, name = "Pilot", code = "S01E01"))
        characterRepository.characterResult = Result.Success(rick(episodeIds = listOf(1)))
        episodeRepository.episodesResult = Result.Success(episodes)

        val result = getCharacterEpisodes(characterId = 1)

        assertEquals(Result.Success(episodes), result)
        assertEquals(listOf(listOf(1)), episodeRepository.requestedIds)
    }

    @Test
    fun `returns the error when the character cannot be loaded`() = runTest {
        characterRepository.characterResult = Result.Failure(DataError.Network.NO_INTERNET)

        val result = getCharacterEpisodes(characterId = 1)

        assertEquals(Result.Failure(DataError.Network.NO_INTERNET), result)
        assertTrue(episodeRepository.requestedIds.isEmpty())
    }

    @Test
    fun `returns the error when the episodes cannot be loaded`() = runTest {
        characterRepository.characterResult = Result.Success(rick(episodeIds = listOf(1)))
        episodeRepository.episodesResult = Result.Failure(DataError.Network.SERVER_ERROR)

        val result = getCharacterEpisodes(characterId = 1)

        assertEquals(Result.Failure(DataError.Network.SERVER_ERROR), result)
    }

    private fun rick(episodeIds: List<Int>) = Character(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        species = "Human",
        type = "",
        gender = CharacterGender.MALE,
        originName = "Earth (C-137)",
        locationName = "Citadel of Ricks",
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        episodeIds = episodeIds,
    )
}
