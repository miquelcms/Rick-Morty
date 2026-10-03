package com.miquelcms.rickmorty.core.testing

import com.miquelcms.rickmorty.core.analytics.AnalyticsEvent
import com.miquelcms.rickmorty.core.analytics.AnalyticsTracker

class FakeAnalyticsTracker : AnalyticsTracker {
    val events = mutableListOf<AnalyticsEvent>()

    override fun track(event: AnalyticsEvent) {
        events += event
    }
}
