package com.miquelcms.rickmorty.core.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ResultTest {

    private val success: Result<Int, DataError> = Result.Success(2)
    private val failure: Result<Int, DataError> = Result.Failure(DataError.Network.NOT_FOUND)

    @Test
    fun `map transforms the data of a success`() {
        assertEquals(Result.Success("2"), success.map { it.toString() })
    }

    @Test
    fun `map keeps the error of a failure`() {
        assertEquals(Result.Failure(DataError.Network.NOT_FOUND), failure.map { it.toString() })
    }

    @Test
    fun `onSuccess runs only for a success`() {
        val received = mutableListOf<Int>()

        success.onSuccess { received += it }
        failure.onSuccess { received += it }

        assertEquals(listOf(2), received)
    }

    @Test
    fun `onFailure runs only for a failure`() {
        val received = mutableListOf<DataError>()

        success.onFailure { received += it }
        failure.onFailure { received += it }

        assertEquals(listOf(DataError.Network.NOT_FOUND), received)
    }
}
