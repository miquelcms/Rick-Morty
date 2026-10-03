package com.miquelcms.rickmorty.feature.characters.ui.detail

sealed interface CharacterDetailAction {
    data object OnRetryClick : CharacterDetailAction
}
