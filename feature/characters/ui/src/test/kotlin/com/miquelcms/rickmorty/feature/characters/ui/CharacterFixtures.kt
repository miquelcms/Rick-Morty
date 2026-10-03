package com.miquelcms.rickmorty.feature.characters.ui

import com.miquelcms.rickmorty.feature.characters.domain.model.Character
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterGender
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterStatus
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterUi

fun character(id: Int, name: String = "Character $id") = Character(
    id = id,
    name = name,
    status = CharacterStatus.ALIVE,
    species = "Human",
    type = "",
    gender = CharacterGender.MALE,
    originName = "Earth (C-137)",
    locationName = "Citadel of Ricks",
    imageUrl = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg",
    episodeIds = listOf(1),
)

fun characterUi(id: Int, name: String = "Character $id") = CharacterUi(
    id = id,
    name = name,
    imageUrl = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg",
    status = CharacterStatusUi.ALIVE,
)

fun episode(id: Int) = Episode(
    id = id,
    name = "Episode $id",
    code = "S01E0$id",
)
