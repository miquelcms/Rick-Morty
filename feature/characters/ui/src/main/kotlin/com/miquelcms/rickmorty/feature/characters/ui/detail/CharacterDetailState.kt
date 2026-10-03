package com.miquelcms.rickmorty.feature.characters.ui.detail

import com.miquelcms.rickmorty.core.ui.UiText
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterDetailUi
import com.miquelcms.rickmorty.feature.characters.ui.model.EpisodeUi

data class CharacterDetailState(
    val character: CharacterDetailUi? = null,
    val episodes: List<EpisodeUi> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingEpisodes: Boolean = false,
    val error: UiText? = null,
    val episodesError: UiText? = null,
)
