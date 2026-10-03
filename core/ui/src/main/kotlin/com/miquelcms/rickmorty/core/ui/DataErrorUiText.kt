package com.miquelcms.rickmorty.core.ui

import com.miquelcms.rickmorty.core.domain.DataError

fun DataError.toUiText(): UiText =
    when (this) {
        DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_request_timeout)
        DataError.Network.NOT_FOUND -> UiText.StringResource(R.string.error_not_found)
        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.error_server)
        DataError.Network.SERIALIZATION,
        DataError.Network.UNKNOWN,
        -> UiText.StringResource(R.string.error_unknown)
    }
