package com.miquelcms.rickmorty.feature.characters.data.mapper

import com.miquelcms.rickmorty.feature.characters.data.remote.dto.CharacterDto
import com.miquelcms.rickmorty.feature.characters.data.remote.dto.CharacterPageDto
import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterGender
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterSpecies
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterStatus

internal fun CharacterPageDto.toCharacterPage() = CharacterPage(
    characters = results.map { it.toCharacter() },
    hasNextPage = info.next != null,
)

internal fun CharacterDto.toCharacter() = Character(
    id = id,
    name = name,
    status = status.toCharacterStatus(),
    species = species,
    type = type,
    gender = gender.toCharacterGender(),
    originName = origin.name,
    locationName = location.name,
    imageUrl = image,
    episodeIds = episode.mapNotNull { url -> url.substringAfterLast('/').toIntOrNull() },
)

internal fun CharacterStatus.toQueryValue() = name.lowercase()

internal fun CharacterGender.toQueryValue() = name.lowercase()

internal fun CharacterSpecies.toQueryValue() =
    when (this) {
        CharacterSpecies.HUMAN -> "Human"
        CharacterSpecies.ALIEN -> "Alien"
        CharacterSpecies.HUMANOID -> "Humanoid"
        CharacterSpecies.ANIMAL -> "Animal"
        CharacterSpecies.ROBOT -> "Robot"
        CharacterSpecies.MYTHOLOGICAL_CREATURE -> "Mythological Creature"
        CharacterSpecies.POOPYBUTTHOLE -> "Poopybutthole"
        CharacterSpecies.CRONENBERG -> "Cronenberg"
        CharacterSpecies.DISEASE -> "Disease"
        CharacterSpecies.UNKNOWN -> "unknown"
    }

private fun String.toCharacterStatus() =
    CharacterStatus.entries.find { it.name.equals(this, ignoreCase = true) }
        ?: CharacterStatus.UNKNOWN

private fun String.toCharacterGender() =
    CharacterGender.entries.find { it.name.equals(this, ignoreCase = true) }
        ?: CharacterGender.UNKNOWN
