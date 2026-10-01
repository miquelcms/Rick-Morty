package com.miquelcms.rickmorty.core.analytics

interface AnalyticsTracker {
    fun track(event: AnalyticsEvent)
}
