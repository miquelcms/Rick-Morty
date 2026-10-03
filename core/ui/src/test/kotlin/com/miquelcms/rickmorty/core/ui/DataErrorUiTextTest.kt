package com.miquelcms.rickmorty.core.ui

import com.miquelcms.rickmorty.core.domain.DataError
import org.junit.Assert.assertEquals
import org.junit.Test

class DataErrorUiTextTest {

    @Test
    fun `maps each network error to its own message`() {
        val expected = mapOf(
            DataError.Network.NO_INTERNET to R.string.error_no_internet,
            DataError.Network.REQUEST_TIMEOUT to R.string.error_request_timeout,
            DataError.Network.NOT_FOUND to R.string.error_not_found,
            DataError.Network.SERVER_ERROR to R.string.error_server,
            DataError.Network.SERIALIZATION to R.string.error_unknown,
            DataError.Network.UNKNOWN to R.string.error_unknown,
        )

        DataError.Network.entries.forEach { error ->
            assertEquals(UiText.StringResource(expected.getValue(error)), error.toUiText())
        }
    }
}
