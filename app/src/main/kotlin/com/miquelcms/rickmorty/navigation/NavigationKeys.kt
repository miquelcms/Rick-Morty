package com.miquelcms.rickmorty.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object CharacterListKey : NavKey

@Serializable
data class CharacterDetailKey(val characterId: Int) : NavKey
