package com.miquelcms.rickmorty.core.data.network

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class HttpCacheTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val server = MockWebServer()
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        server.start()
        client = createOkHttpClient(temporaryFolder.root)
    }

    @After
    fun tearDown() {
        server.close()
    }

    @Test
    fun `a cacheable response is served from disk the second time`() {
        enqueueCacheableResponse()

        val first = get()
        val second = get()

        assertEquals("Rick", first)
        assertEquals("Rick", second)
        assertEquals(1, server.requestCount)
    }

    @Test
    fun `a no-cache request goes to the server again`() {
        enqueueCacheableResponse()
        enqueueCacheableResponse()

        get()
        get(CacheControl.FORCE_NETWORK)

        assertEquals(2, server.requestCount)
    }

    private fun enqueueCacheableResponse() {
        server.enqueue(
            MockResponse.Builder()
                .addHeader("Cache-Control", "public, max-age=60")
                .body("Rick")
                .build(),
        )
    }

    private fun get(cacheControl: CacheControl? = null): String {
        val request = Request.Builder()
            .url(server.url("/character"))
            .apply { cacheControl?.let(::cacheControl) }
            .build()
        return client.newCall(request).execute().use { it.body.string() }
    }
}
