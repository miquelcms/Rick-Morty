package com.miquelcms.rickmorty.feature.characters.domain.model

data class CharacterFilters(
    val name: String = "",
    val status: CharacterStatus? = null,
    val species: String = "",
    val type: String = "",
    val gender: CharacterGender? = null,
)
