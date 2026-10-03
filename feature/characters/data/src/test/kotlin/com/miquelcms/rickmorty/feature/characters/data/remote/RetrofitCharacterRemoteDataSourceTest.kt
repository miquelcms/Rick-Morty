package com.miquelcms.rickmorty.feature.characters.data.remote

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.core.testing.FakeErrorReporter
import com.miquelcms.rickmorty.core.testing.createRetrofit
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterGender
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterSpecies
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

class RetrofitCharacterRemoteDataSourceTest {

    private val server = MockWebServer()
    private lateinit var dataSource: RetrofitCharacterRemoteDataSource

    @Before
    fun setUp() {
        server.start()
        dataSource = RetrofitCharacterRemoteDataSource(
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

        val result = dataSource.getCharacters(1, CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Success(CharacterPage(listOf(RICK), nextPage = 2)), result)
    }

    @Test
    fun `requests the first page when no page is given`() = runTest {
        server.enqueue(MockResponse.Builder().body(PAGE_JSON).build())

        val result = dataSource.getCharacters(null, CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Success(CharacterPage(listOf(RICK), nextPage = 2)), result)
        assertEquals("/character?page=1", server.takeRequest().target)
    }

    @Test
    fun `sends the page and only the filters that are set`() = runTest {
        server.enqueue(MockResponse.Builder().body(PAGE_JSON).build())
        val filters = CharacterFilters(
            name = "rick",
            status = CharacterStatus.ALIVE,
            species = CharacterSpecies.MYTHOLOGICAL_CREATURE,
            gender = CharacterGender.MALE,
        )

        dataSource.getCharacters(2, filters, forceRefresh = false)

        val request = server.takeRequest()
        assertEquals(
            "/character?page=2&name=rick&status=alive&species=Mythological%20Creature&gender=male",
            request.target,
        )
        assertNull(request.headers["Cache-Control"])
    }

    @Test
    fun `keeps only the characters of the requested species`() = runTest {
        val humanoidJson = RICK_JSON
            .replace("\"id\": 1", "\"id\": 2")
            .replace("\"Human\"", "\"Humanoid\"")
        val pageJson = """{ "info": { "next": null }, "results": [$RICK_JSON, $humanoidJson] }"""
        server.enqueue(MockResponse.Builder().body(pageJson).build())
        val filters = CharacterFilters(species = CharacterSpecies.HUMAN)

        val result = dataSource.getCharacters(1, filters, forceRefresh = false)

        assertEquals(Result.Success(CharacterPage(listOf(RICK), nextPage = null)), result)
    }

    @Test
    fun `asks the server to skip the cache when forcing a refresh`() = runTest {
        server.enqueue(MockResponse.Builder().body(PAGE_JSON).build())

        dataSource.getCharacters(1, CharacterFilters(), forceRefresh = true)

        assertEquals("no-cache", server.takeRequest().headers["Cache-Control"])
    }

    @Test
    fun `returns an empty page when the api finds nothing`() = runTest {
        server.enqueue(MockResponse.Builder().code(404).body(NOTHING_JSON).build())
        val filters = CharacterFilters(name = "zzz")

        val result = dataSource.getCharacters(1, filters, forceRefresh = false)

        assertEquals(Result.Success(CharacterPage(emptyList(), nextPage = null)), result)
    }

    @Test
    fun `returns the error when the page request fails`() = runTest {
        server.enqueue(MockResponse.Builder().code(500).build())

        val result = dataSource.getCharacters(1, CharacterFilters(), forceRefresh = false)

        assertEquals(Result.Failure(DataError.Network.SERVER_ERROR), result)
    }

    @Test
    fun `requests a character by its id`() = runTest {
        server.enqueue(MockResponse.Builder().body(RICK_JSON).build())

        val result = dataSource.getCharacter(1)

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
