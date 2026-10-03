package com.miquelcms.rickmorty.feature.characters.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miquelcms.rickmorty.core.analytics.AnalyticsTracker
import com.miquelcms.rickmorty.core.domain.onFailure
import com.miquelcms.rickmorty.core.domain.onSuccess
import com.miquelcms.rickmorty.core.ui.toUiText
import com.miquelcms.rickmorty.feature.characters.domain.repository.CharacterRepository
import com.miquelcms.rickmorty.feature.characters.ui.analytics.CharactersAnalytics
import com.miquelcms.rickmorty.feature.characters.ui.mapper.toCharacterFilters
import com.miquelcms.rickmorty.feature.characters.ui.mapper.toCharacterUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterFiltersUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterSpeciesUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class CharacterListViewModel(
    private val characterRepository: CharacterRepository,
    private val analyticsTracker: AnalyticsTracker,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(
        CharacterListState(
            query = savedStateHandle[KEY_QUERY] ?: "",
            filters = restoreFilters(),
        ),
    )
    val state = _state.asStateFlow()

    private val _events = Channel<CharacterListEvent>()
    val events = _events.receiveAsFlow()

    private var nextPage = FIRST_PAGE
    private var hasNextPage = false
    private var loadJob: Job? = null
    private var searchJob: Job? = null

    init {
        analyticsTracker.track(
            CharactersAnalytics.screenView(CharactersAnalytics.SCREEN_CHARACTER_LIST),
        )
        loadFirstPage()
    }

    fun onAction(action: CharacterListAction) {
        when (action) {
            is CharacterListAction.OnQueryChange -> changeQuery(action.query)
            is CharacterListAction.OnFiltersApply -> applyFilters(action.filters)
            CharacterListAction.OnClearSearchClick -> clearSearch()
            CharacterListAction.OnRefresh -> loadFirstPage(isRefresh = true)
            CharacterListAction.OnLoadNextPage -> loadNextPage()
            CharacterListAction.OnRetryClick -> retry()
            is CharacterListAction.OnCharacterClick -> openCharacter(action.characterId)
        }
    }

    private fun changeQuery(query: String) {
        _state.update { it.copy(query = query) }
        savedStateHandle[KEY_QUERY] = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS.milliseconds)
            if (query.isNotBlank()) analyticsTracker.track(CharactersAnalytics.search(query))
            loadFirstPage()
        }
    }

    private fun applyFilters(filters: CharacterFiltersUi) {
        _state.update { it.copy(filters = filters) }
        saveFilters(filters)
        analyticsTracker.track(CharactersAnalytics.filtersApplied(filters))
        loadFirstPage()
    }

    private fun clearSearch() {
        searchJob?.cancel()
        _state.update { it.copy(query = "", filters = CharacterFiltersUi()) }
        savedStateHandle[KEY_QUERY] = ""
        saveFilters(CharacterFiltersUi())
        loadFirstPage()
    }

    private fun retry() {
        if (state.value.error != null) loadFirstPage() else loadNextPage(isRetry = true)
    }

    private fun openCharacter(characterId: Int) {
        analyticsTracker.track(CharactersAnalytics.characterOpened(characterId))
        viewModelScope.launch {
            _events.send(CharacterListEvent.NavigateToDetail(characterId))
        }
    }

    private fun loadFirstPage(isRefresh: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = !isRefresh,
                    isRefreshing = isRefresh,
                    isLoadingNextPage = false,
                    error = null,
                    nextPageError = null,
                )
            }
            val current = state.value
            characterRepository.getCharacters(
                page = FIRST_PAGE,
                filters = current.filters.toCharacterFilters(name = current.query),
                forceRefresh = isRefresh,
            ).onSuccess { page ->
                nextPage = FIRST_PAGE + 1
                hasNextPage = page.hasNextPage
                _state.update {
                    it.copy(
                        characters = page.characters.map { character -> character.toCharacterUi() },
                        isLoading = false,
                        isRefreshing = false,
                    )
                }
            }.onFailure { error ->
                if (isRefresh) {
                    _state.update { it.copy(isRefreshing = false) }
                    _events.send(CharacterListEvent.ShowMessage(error.toUiText()))
                } else {
                    _state.update {
                        it.copy(
                            characters = emptyList(),
                            isLoading = false,
                            error = error.toUiText(),
                        )
                    }
                }
            }
        }
    }

    private fun loadNextPage(isRetry: Boolean = false) {
        val current = state.value
        val isBusy = current.isLoading || current.isRefreshing || current.isLoadingNextPage
        val isWaitingForRetry = current.nextPageError != null && !isRetry
        if (isBusy || isWaitingForRetry || !hasNextPage) return

        loadJob = viewModelScope.launch {
            _state.update { it.copy(isLoadingNextPage = true, nextPageError = null) }
            characterRepository.getCharacters(
                page = nextPage,
                filters = current.filters.toCharacterFilters(name = current.query),
                forceRefresh = false,
            ).onSuccess { page ->
                nextPage++
                hasNextPage = page.hasNextPage
                _state.update {
                    it.copy(
                        characters = it.characters +
                            page.characters.map { character -> character.toCharacterUi() },
                        isLoadingNextPage = false,
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(isLoadingNextPage = false, nextPageError = error.toUiText())
                }
            }
        }
    }

    private fun restoreFilters() = CharacterFiltersUi(
        status = savedStateHandle.get<String>(KEY_STATUS)?.let(CharacterStatusUi::valueOf),
        gender = savedStateHandle.get<String>(KEY_GENDER)?.let(CharacterGenderUi::valueOf),
        species = savedStateHandle.get<String>(KEY_SPECIES)?.let(CharacterSpeciesUi::valueOf),
    )

    private fun saveFilters(filters: CharacterFiltersUi) {
        savedStateHandle[KEY_STATUS] = filters.status?.name
        savedStateHandle[KEY_GENDER] = filters.gender?.name
        savedStateHandle[KEY_SPECIES] = filters.species?.name
    }

    private companion object {
        const val FIRST_PAGE = 1
        const val SEARCH_DEBOUNCE_MILLIS = 400L
        const val KEY_QUERY = "query"
        const val KEY_STATUS = "status"
        const val KEY_GENDER = "gender"
        const val KEY_SPECIES = "species"
    }
}
