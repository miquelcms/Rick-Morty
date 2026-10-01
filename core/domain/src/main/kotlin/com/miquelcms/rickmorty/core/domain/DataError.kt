package com.miquelcms.rickmorty.core.domain

sealed interface DataError {

    enum class Network : DataError {
        NO_INTERNET,
        REQUEST_TIMEOUT,
        NOT_FOUND,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN,
    }
}
