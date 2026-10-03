package com.miquelcms.rickmorty.feature.characters.ui.model

data class CharacterDetailUi(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: CharacterStatusUi,
    val species: String,
    val type: String?,
    val gender: CharacterGenderUi,
    val originName: String,
    val locationName: String,
)
