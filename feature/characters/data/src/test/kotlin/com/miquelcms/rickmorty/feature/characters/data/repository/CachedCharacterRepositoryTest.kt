package com.miquelcms.rickmorty.feature.characters.data.repository

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.data.remote.FakeCharacterRemoteDataSource
import com.miquelcms.rickmorty.feature.characters.data.remote.FakeCharacterRemoteDataSource.PageRequest
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterGender
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterStatus
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CachedCharacterRepositoryTest {

    private val remoteDataSource = FakeCharacterRemoteDataSource()
    private val repository = CachedCharacterRepository(remoteDataSource)

    private val rick = character(id = 1)
    private val morty = character(id = 2)

    @Test
    fun `asks for the first page without a page number`() = runTest {
        val page = CharacterPage(listOf(rick), nextPage = 2)
        remoteDataSource.firstPageResult = Result.Success(page)

        val result = repository.getCharacters(CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Success(page), result)
        assertEquals(listOf(null), remoteDataSource.pageRequests.map { it.page })
    }

    @Test
    fun `requests the given page with the filters and the refresh flag`() = runTest {
        remoteDataSource.nextPageResults[3] = Result.Success(CharacterPage(listOf(rick), null))
        val filters = CharacterFilters(name = "rick", status = CharacterStatus.ALIVE)

        repository.getCharacters(filters, forceRefresh = true, page = 3)

        assertEquals(listOf(PageRequest(3, filters, true)), remoteDataSource.pageRequests)
    }

    @Test
    fun `skips the pages that have no characters`() = runTest {
        val lastPage = CharacterPage(listOf(morty), nextPage = null)
        remoteDataSource.firstPageResult = Result.Success(CharacterPage(emptyList(), 2))
        remoteDataSource.nextPageResults[2] = Result.Success(CharacterPage(emptyList(), 3))
        remoteDataSource.nextPageResults[3] = Result.Success(lastPage)

        val result = repository.getCharacters(CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Success(lastPage), result)
        assertEquals(listOf(null, 2, 3), remoteDataSource.pageRequests.map { it.page })
    }

    @Test
    fun `returns an empty page when there are no more pages to try`() = runTest {
        val emptyPage = CharacterPage(emptyList(), nextPage = null)
        remoteDataSource.firstPageResult = Result.Success(emptyPage)

        val result = repository.getCharacters(CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Success(emptyPage), result)
    }

    @Test
    fun `returns the error of the page that fails`() = runTest {
        remoteDataSource.firstPageResult = Result.Failure(DataError.Network.NO_INTERNET)

        val result = repository.getCharacters(CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Failure(DataError.Network.NO_INTERNET), result)
    }

    @Test
    fun `returns a character already loaded in a page without asking again`() = runTest {
        remoteDataSource.firstPageResult = Result.Success(CharacterPage(listOf(rick), null))
        repository.getCharacters(CharacterFilters(), forceRefresh = false)

        val result = repository.getCharacter(1)

        assertEquals(Result.Success(rick), result)
        assertTrue(remoteDataSource.characterRequests.isEmpty())
    }

    @Test
    fun `asks for a character only the first time`() = runTest {
        remoteDataSource.characterResults[2] = Result.Success(morty)

        val first = repository.getCharacter(2)
        val second = repository.getCharacter(2)

        assertEquals(Result.Success(morty), first)
        assertEquals(Result.Success(morty), second)
        assertEquals(listOf(2), remoteDataSource.characterRequests)
    }

    @Test
    fun `returns the error when the character cannot be loaded`() = runTest {
        remoteDataSource.characterResults[2] = Result.Failure(DataError.Network.SERVER_ERROR)

        val result = repository.getCharacter(2)

        assertEquals(Result.Failure(DataError.Network.SERVER_ERROR), result)
    }

    private fun character(id: Int) = Character(
        id = id,
        name = "Character $id",
        status = CharacterStatus.ALIVE,
        species = "Human",
        type = "",
        gender = CharacterGender.MALE,
        originName = "Earth (C-137)",
        locationName = "Citadel of Ricks",
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg",
        episodeIds = listOf(1),
    )
}
