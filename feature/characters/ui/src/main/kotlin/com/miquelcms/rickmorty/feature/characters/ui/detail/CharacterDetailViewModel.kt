package com.miquelcms.rickmorty.feature.characters.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miquelcms.rickmorty.core.analytics.AnalyticsTracker
import com.miquelcms.rickmorty.core.domain.onFailure
import com.miquelcms.rickmorty.core.domain.onSuccess
import com.miquelcms.rickmorty.core.ui.toUiText
import com.miquelcms.rickmorty.feature.characters.domain.repository.CharacterRepository
import com.miquelcms.rickmorty.feature.characters.domain.usecase.GetCharacterEpisodesUseCase
import com.miquelcms.rickmorty.feature.characters.ui.analytics.CharactersAnalytics
import com.miquelcms.rickmorty.feature.characters.ui.mapper.toCharacterDetailUi
import com.miquelcms.rickmorty.feature.characters.ui.mapper.toEpisodeUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val characterId: Int,
    private val characterRepository: CharacterRepository,
    private val getCharacterEpisodes: GetCharacterEpisodesUseCase,
    analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterDetailState())
    val state = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        analyticsTracker.track(
            CharactersAnalytics.screenView(CharactersAnalytics.SCREEN_CHARACTER_DETAIL),
        )
        analyticsTracker.track(CharactersAnalytics.characterOpened(characterId))
        loadCharacter()
    }

    fun onAction(action: CharacterDetailAction) {
        when (action) {
            CharacterDetailAction.OnRetryClick -> retry()
        }
    }

    private fun retry() {
        if (state.value.error != null) loadCharacter() else loadEpisodes()
    }

    private fun loadCharacter() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            characterRepository.getCharacter(characterId)
                .onSuccess { character ->
                    _state.update {
                        it.copy(character = character.toCharacterDetailUi(), isLoading = false)
                    }
                    fetchEpisodes()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }

    private fun loadEpisodes() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch { fetchEpisodes() }
    }

    private suspend fun fetchEpisodes() {
        _state.update { it.copy(isLoadingEpisodes = true, episodesError = null) }
        getCharacterEpisodes(characterId)
            .onSuccess { episodes ->
                _state.update {
                    it.copy(
                        episodes = episodes.map { episode -> episode.toEpisodeUi() },
                        isLoadingEpisodes = false,
                    )
                }
            }
            .onFailure { error ->
                _state.update {
                    it.copy(isLoadingEpisodes = false, episodesError = error.toUiText())
                }
            }
    }
}
