package com.miquelcms.rickmorty.feature.characters.ui.list

import com.miquelcms.rickmorty.core.ui.UiText

sealed interface CharacterListEvent {
    data class NavigateToDetail(val characterId: Int) : CharacterListEvent
    data class ShowMessage(val message: UiText) : CharacterListEvent
}
