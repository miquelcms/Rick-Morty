package com.miquelcms.rickmorty.feature.characters.ui.mapper

import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterGender
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterStatus
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterFiltersUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterUi

internal fun Character.toCharacterUi() = CharacterUi(
    id = id,
    name = name,
    imageUrl = imageUrl,
    status = status.toCharacterStatusUi(),
)

internal fun CharacterStatus.toCharacterStatusUi(): CharacterStatusUi =
    when (this) {
        CharacterStatus.ALIVE -> CharacterStatusUi.ALIVE
        CharacterStatus.DEAD -> CharacterStatusUi.DEAD
        CharacterStatus.UNKNOWN -> CharacterStatusUi.UNKNOWN
    }

internal fun CharacterGender.toCharacterGenderUi(): CharacterGenderUi =
    when (this) {
        CharacterGender.FEMALE -> CharacterGenderUi.FEMALE
        CharacterGender.MALE -> CharacterGenderUi.MALE
        CharacterGender.GENDERLESS -> CharacterGenderUi.GENDERLESS
        CharacterGender.UNKNOWN -> CharacterGenderUi.UNKNOWN
    }

internal fun CharacterFiltersUi.toCharacterFilters(name: String) = CharacterFilters(
    name = name.trim(),
    status = status?.toCharacterStatus(),
    species = species.trim(),
    type = type.trim(),
    gender = gender?.toCharacterGender(),
)

private fun CharacterStatusUi.toCharacterStatus(): CharacterStatus =
    when (this) {
        CharacterStatusUi.ALIVE -> CharacterStatus.ALIVE
        CharacterStatusUi.DEAD -> CharacterStatus.DEAD
        CharacterStatusUi.UNKNOWN -> CharacterStatus.UNKNOWN
    }

private fun CharacterGenderUi.toCharacterGender(): CharacterGender =
    when (this) {
        CharacterGenderUi.FEMALE -> CharacterGender.FEMALE
        CharacterGenderUi.MALE -> CharacterGender.MALE
        CharacterGenderUi.GENDERLESS -> CharacterGender.GENDERLESS
        CharacterGenderUi.UNKNOWN -> CharacterGender.UNKNOWN
    }
