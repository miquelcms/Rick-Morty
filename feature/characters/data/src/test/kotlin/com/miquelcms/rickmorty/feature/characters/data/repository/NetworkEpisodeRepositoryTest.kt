package com.miquelcms.rickmorty.feature.characters.data.repository

import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.core.testing.FakeErrorReporter
import com.miquelcms.rickmorty.core.testing.createRetrofit
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.create

class NetworkEpisodeRepositoryTest {

    private val server = MockWebServer()
    private lateinit var repository: NetworkEpisodeRepository

    @Before
    fun setUp() {
        server.start()
        repository = NetworkEpisodeRepository(
            api = server.createRetrofit().create(),
            errorReporter = FakeErrorReporter(),
        )
    }

    @After
    fun tearDown() {
        server.close()
    }

    @Test
    fun `requests all the ids in one call and maps the episodes`() = runTest {
        server.enqueue(MockResponse.Builder().body(EPISODES_JSON).build())

        val result = repository.getEpisodes(listOf(1, 2))

        val expected = listOf(
            Episode(id = 1, name = "Pilot", code = "S01E01"),
            Episode(id = 2, name = "Lawnmower Dog", code = "S01E02"),
        )
        assertEquals(Result.Success(expected), result)
        assertEquals("/episode/[1,2]", server.takeRequest().target)
    }

    @Test
    fun `returns no episodes without calling the api when there are no ids`() = runTest {
        val result = repository.getEpisodes(emptyList())

        assertEquals(Result.Success(emptyList<Episode>()), result)
        assertEquals(0, server.requestCount)
    }

    private companion object {
        const val EPISODES_JSON = """
            [
              { "id": 1, "name": "Pilot", "air_date": "December 2, 2013", "episode": "S01E01" },
              { "id": 2, "name": "Lawnmower Dog", "air_date": "", "episode": "S01E02" }
            ]
        """
    }
}
