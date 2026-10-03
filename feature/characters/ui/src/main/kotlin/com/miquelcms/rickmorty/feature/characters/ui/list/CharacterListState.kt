package com.miquelcms.rickmorty.feature.characters.ui.list

import com.miquelcms.rickmorty.core.ui.UiText
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterFiltersUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterUi

data class CharacterListState(
    val query: String = "",
    val filters: CharacterFiltersUi = CharacterFiltersUi(),
    val characters: List<CharacterUi> = emptyList(),
    val isOffline: Boolean = false,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val error: UiText? = null,
    val nextPageError: UiText? = null,
)
