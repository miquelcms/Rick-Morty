package com.miquelcms.rickmorty.core.data.network

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.core.testing.FakeErrorReporter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class SafeCallTest {

    private val errorReporter = FakeErrorReporter()

    @Test
    fun `returns success with the value of the call`() = runTest {
        val result = safeCall(errorReporter) { "Rick" }

        assertEquals(Result.Success("Rick"), result)
    }

    @Test
    fun `maps http 404 to NOT_FOUND`() = runTest {
        val result = safeCall(errorReporter) { throw httpException(404) }

        assertEquals(Result.Failure(DataError.Network.NOT_FOUND), result)
    }

    @Test
    fun `maps http 5xx to SERVER_ERROR`() = runTest {
        val result = safeCall(errorReporter) { throw httpException(503) }

        assertEquals(Result.Failure(DataError.Network.SERVER_ERROR), result)
    }

    @Test
    fun `maps an unexpected http code to UNKNOWN and reports it`() = runTest {
        val exception = httpException(418)

        val result = safeCall(errorReporter) { throw exception }

        assertEquals(Result.Failure(DataError.Network.UNKNOWN), result)
        assertEquals(listOf<Throwable>(exception), errorReporter.reported)
    }

    @Test
    fun `maps a socket timeout to REQUEST_TIMEOUT`() = runTest {
        val result = safeCall(errorReporter) { throw SocketTimeoutException() }

        assertEquals(Result.Failure(DataError.Network.REQUEST_TIMEOUT), result)
    }

    @Test
    fun `maps an io exception to NO_INTERNET without reporting it`() = runTest {
        val result = safeCall(errorReporter) { throw IOException() }

        assertEquals(Result.Failure(DataError.Network.NO_INTERNET), result)
        assertTrue(errorReporter.reported.isEmpty())
    }

    @Test
    fun `maps a serialization exception to SERIALIZATION and reports it`() = runTest {
        val exception = SerializationException("Unexpected field")

        val result = safeCall(errorReporter) { throw exception }

        assertEquals(Result.Failure(DataError.Network.SERIALIZATION), result)
        assertEquals(listOf<Throwable>(exception), errorReporter.reported)
    }

    @Test
    fun `maps any other exception to UNKNOWN and reports it`() = runTest {
        val exception = IllegalStateException()

        val result = safeCall(errorReporter) { throw exception }

        assertEquals(Result.Failure(DataError.Network.UNKNOWN), result)
        assertEquals(listOf<Throwable>(exception), errorReporter.reported)
    }

    @Test
    fun `rethrows a cancellation`() {
        assertThrows(CancellationException::class.java) {
            runTest {
                safeCall(errorReporter) { throw CancellationException() }
            }
        }
    }

    private fun httpException(code: Int) =
        HttpException(Response.error<String>(code, "".toResponseBody()))
}
