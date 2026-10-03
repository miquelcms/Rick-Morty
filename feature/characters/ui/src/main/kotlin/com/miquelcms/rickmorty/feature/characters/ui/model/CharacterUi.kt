package com.miquelcms.rickmorty.feature.characters.ui.model

data class CharacterUi(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: CharacterStatusUi,
)
