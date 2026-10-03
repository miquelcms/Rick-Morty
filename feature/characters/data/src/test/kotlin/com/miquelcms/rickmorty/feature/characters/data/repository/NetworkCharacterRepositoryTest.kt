package com.miquelcms.rickmorty.feature.characters.data.repository

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.data.FakeErrorReporter
import com.miquelcms.rickmorty.feature.characters.data.createRetrofit
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterGender
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterStatus
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.create

class NetworkCharacterRepositoryTest {

    private val server = MockWebServer()
    private lateinit var repository: NetworkCharacterRepository

    @Before
    fun setUp() {
        server.start()
        repository = NetworkCharacterRepository(
            api = server.createRetrofit().create(),
            errorReporter = FakeErrorReporter(),
        )
    }

    @After
    fun tearDown() {
        server.close()
    }

    @Test
    fun `maps a page of characters`() = runTest {
        server.enqueue(MockResponse.Builder().body(PAGE_JSON).build())

        val result = repository.getCharacters(1, CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Success(CharacterPage(listOf(RICK), hasNextPage = true)), result)
    }

    @Test
    fun `sends the page and only the filters that are set`() = runTest {
        server.enqueue(MockResponse.Builder().body(PAGE_JSON).build())
        val filters = CharacterFilters(
            name = "rick",
            status = CharacterStatus.ALIVE,
            gender = CharacterGender.MALE,
        )

        repository.getCharacters(2, filters, forceRefresh = false)

        val request = server.takeRequest()
        assertEquals("/character?page=2&name=rick&status=alive&gender=male", request.target)
        assertNull(request.headers["Cache-Control"])
    }

    @Test
    fun `asks the server to skip the cache when forcing a refresh`() = runTest {
        server.enqueue(MockResponse.Builder().body(PAGE_JSON).build())

        repository.getCharacters(1, CharacterFilters(), forceRefresh = true)

        assertEquals("no-cache", server.takeRequest().headers["Cache-Control"])
    }

    @Test
    fun `returns an empty page when the api finds nothing`() = runTest {
        server.enqueue(MockResponse.Builder().code(404).body(NOTHING_JSON).build())
        val filters = CharacterFilters(name = "zzz")

        val result = repository.getCharacters(1, filters, forceRefresh = false)

        assertEquals(Result.Success(CharacterPage(emptyList(), hasNextPage = false)), result)
    }

    @Test
    fun `returns the error when the page request fails`() = runTest {
        server.enqueue(MockResponse.Builder().code(500).build())

        val result = repository.getCharacters(1, CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Failure(DataError.Network.SERVER_ERROR), result)
    }

    @Test
    fun `returns a character already loaded in a page without calling the api`() = runTest {
        server.enqueue(MockResponse.Builder().body(PAGE_JSON).build())
        repository.getCharacters(1, CharacterFilters(), forceRefresh = false)

        val result = repository.getCharacter(1)

        assertEquals(Result.Success(RICK), result)
        assertEquals(1, server.requestCount)
    }

    @Test
    fun `requests a character that is not cached`() = runTest {
        server.enqueue(MockResponse.Builder().body(RICK_JSON).build())

        val result = repository.getCharacter(1)

        assertEquals(Result.Success(RICK), result)
        assertEquals("/character/1", server.takeRequest().target)
    }

    private companion object {
        val RICK = Character(
            id = 1,
            name = "Rick Sanchez",
            status = CharacterStatus.ALIVE,
            species = "Human",
            type = "",
            gender = CharacterGender.MALE,
            originName = "Earth (C-137)",
            locationName = "Citadel of Ricks",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episodeIds = listOf(1, 2),
        )

        const val RICK_JSON = """
            {
              "id": 1,
              "name": "Rick Sanchez",
              "status": "Alive",
              "species": "Human",
              "type": "",
              "gender": "Male",
              "origin": { "name": "Earth (C-137)", "url": "" },
              "location": { "name": "Citadel of Ricks", "url": "" },
              "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
              "episode": [
                "https://rickandmortyapi.com/api/episode/1",
                "https://rickandmortyapi.com/api/episode/2"
              ],
              "url": "https://rickandmortyapi.com/api/character/1",
              "created": "2017-11-04T18:48:46.250Z"
            }
        """

        const val PAGE_JSON = """
            {
              "info": {
                "count": 826,
                "pages": 42,
                "next": "https://rickandmortyapi.com/api/character?page=2",
                "prev": null
              },
              "results": [$RICK_JSON]
            }
        """

        const val NOTHING_JSON = """{"error":"There is nothing here"}"""
    }
}
