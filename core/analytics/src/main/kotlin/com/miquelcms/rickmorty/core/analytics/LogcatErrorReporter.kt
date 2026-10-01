package com.miquelcms.rickmorty.core.analytics

import android.util.Log

internal class LogcatErrorReporter : ErrorReporter {

    // TODO: Record the error in Firebase Crashlytics
    override fun report(throwable: Throwable) {
        Log.e(TAG, throwable.message, throwable)
    }

    private companion object {
        const val TAG = "ErrorReporter"
    }
}
