package com.miquelcms.rickmorty.feature.characters.domain.model

data class CharacterPage(
    val characters: List<Character>,
    val nextPage: Int?,
)
