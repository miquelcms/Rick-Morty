package com.miquelcms.rickmorty.feature.characters.ui.model

import androidx.annotation.StringRes
import com.miquelcms.rickmorty.feature.characters.ui.R

enum class CharacterSpeciesUi(@StringRes val labelRes: Int) {
    HUMAN(R.string.character_species_human),
    ALIEN(R.string.character_species_alien),
    HUMANOID(R.string.character_species_humanoid),
    ANIMAL(R.string.character_species_animal),
    ROBOT(R.string.character_species_robot),
    MYTHOLOGICAL_CREATURE(R.string.character_species_mythological_creature),
    POOPYBUTTHOLE(R.string.character_species_poopybutthole),
    CRONENBERG(R.string.character_species_cronenberg),
    DISEASE(R.string.character_species_disease),
    UNKNOWN(R.string.character_species_unknown),
}
