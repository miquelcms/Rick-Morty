package com.miquelcms.rickmorty.feature.characters.data

import com.miquelcms.rickmorty.core.analytics.ErrorReporter

class FakeErrorReporter : ErrorReporter {
    val reported = mutableListOf<Throwable>()

    override fun report(throwable: Throwable) {
        reported += throwable
    }
}
