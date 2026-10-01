package com.miquelcms.rickmorty.core.analytics

interface ErrorReporter {
    fun report(throwable: Throwable)
}
