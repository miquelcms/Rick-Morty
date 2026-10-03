package com.miquelcms.rickmorty.feature.characters.ui.analytics

import com.miquelcms.rickmorty.core.analytics.AnalyticsEvent
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterFiltersUi

internal object CharactersAnalytics {

    fun screenView(screenName: String) = AnalyticsEvent(
        name = "screen_view",
        params = mapOf("screen_name" to screenName),
    )

    fun search(query: String) = AnalyticsEvent(
        name = "search",
        params = mapOf("query" to query),
    )

    fun filtersApplied(filters: CharacterFiltersUi) = AnalyticsEvent(
        name = "filters_applied",
        params = buildMap {
            filters.status?.let { put("status", it.name.lowercase()) }
            filters.gender?.let { put("gender", it.name.lowercase()) }
            if (filters.species.isNotBlank()) put("species", filters.species)
            if (filters.type.isNotBlank()) put("type", filters.type)
        },
    )

    fun characterOpened(characterId: Int) = AnalyticsEvent(
        name = "character_opened",
        params = mapOf("character_id" to characterId.toString()),
    )

    const val SCREEN_CHARACTER_LIST = "character_list"
}
