package com.miquelcms.rickmorty.feature.characters.ui.model

data class CharacterFiltersUi(
    val status: CharacterStatusUi? = null,
    val gender: CharacterGenderUi? = null,
    val species: CharacterSpeciesUi? = null,
) {
    val activeCount: Int
        get() = listOfNotNull(status, gender, species).size
}
