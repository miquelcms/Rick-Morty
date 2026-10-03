package com.miquelcms.rickmorty.feature.characters.domain.model

data class CharacterFilters(
    val name: String = "",
    val status: CharacterStatus? = null,
    val species: CharacterSpecies? = null,
    val gender: CharacterGender? = null,
)
