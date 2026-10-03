package com.miquelcms.rickmorty.navigation

import androidx.navigation3.runtime.NavKey

internal fun MutableList<NavKey>.openCharacterDetail(characterId: Int) {
    if (lastOrNull() !is CharacterDetailKey) add(CharacterDetailKey(characterId))
}

internal fun MutableList<NavKey>.goBack() {
    if (size > 1) removeLastOrNull()
}
