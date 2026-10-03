package com.miquelcms.rickmorty.feature.characters.ui.list

import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterFiltersUi

sealed interface CharacterListAction {
    data class OnQueryChange(val query: String) : CharacterListAction
    data class OnFiltersApply(val filters: CharacterFiltersUi) : CharacterListAction
    data object OnClearSearchClick : CharacterListAction
    data object OnRefresh : CharacterListAction
    data object OnLoadNextPage : CharacterListAction
    data object OnRetryClick : CharacterListAction
    data class OnCharacterClick(val characterId: Int) : CharacterListAction
}
