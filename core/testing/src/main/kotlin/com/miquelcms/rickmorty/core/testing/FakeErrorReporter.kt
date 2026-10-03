package com.miquelcms.rickmorty.core.testing

import com.miquelcms.rickmorty.core.analytics.ErrorReporter

class FakeErrorReporter : ErrorReporter {
    val reported = mutableListOf<Throwable>()

    override fun report(throwable: Throwable) {
        reported += throwable
    }
}
