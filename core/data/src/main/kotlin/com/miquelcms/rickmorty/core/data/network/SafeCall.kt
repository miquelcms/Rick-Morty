package com.miquelcms.rickmorty.core.data.network

import com.miquelcms.rickmorty.core.analytics.ErrorReporter
import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

suspend fun <T> safeCall(
    errorReporter: ErrorReporter,
    call: suspend () -> T,
): Result<T, DataError.Network> =
    try {
        Result.Success(call())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        Result.Failure(e.toDataError(errorReporter))
    } catch (_: SocketTimeoutException) {
        Result.Failure(DataError.Network.REQUEST_TIMEOUT)
    } catch (_: IOException) {
        Result.Failure(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        errorReporter.report(e)
        Result.Failure(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        errorReporter.report(e)
        Result.Failure(DataError.Network.UNKNOWN)
    }

private fun HttpException.toDataError(errorReporter: ErrorReporter): DataError.Network =
    when (code()) {
        HttpURLConnection.HTTP_NOT_FOUND -> DataError.Network.NOT_FOUND
        HttpURLConnection.HTTP_CLIENT_TIMEOUT -> DataError.Network.REQUEST_TIMEOUT
        in 500..599 -> DataError.Network.SERVER_ERROR
        else -> {
            errorReporter.report(this)
            DataError.Network.UNKNOWN
        }
    }
