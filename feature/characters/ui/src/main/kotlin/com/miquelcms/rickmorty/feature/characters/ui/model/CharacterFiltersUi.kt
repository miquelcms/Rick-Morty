package com.miquelcms.rickmorty.feature.characters.ui.model

data class CharacterFiltersUi(
    val status: CharacterStatusUi? = null,
    val gender: CharacterGenderUi? = null,
    val species: String = "",
    val type: String = "",
) {
    val activeCount: Int
        get() = listOf(status != null, gender != null, species.isNotBlank(), type.isNotBlank())
            .count { it }
}
