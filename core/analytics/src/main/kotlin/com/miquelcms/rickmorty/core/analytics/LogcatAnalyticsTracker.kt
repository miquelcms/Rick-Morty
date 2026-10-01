package com.miquelcms.rickmorty.core.analytics

import android.util.Log

internal class LogcatAnalyticsTracker : AnalyticsTracker {

    // TODO: Send the event to Firebase Analytics
    override fun track(event: AnalyticsEvent) {
        Log.d(TAG, "${event.name} ${event.params}")
    }

    private companion object {
        const val TAG = "Analytics"
    }
}
