package com.miquelcms.rickmorty.core.analytics

data class AnalyticsEvent(
    val name: String,
    val params: Map<String, String> = emptyMap(),
)
