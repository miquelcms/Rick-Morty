package com.miquelcms.rickmorty.feature.characters.ui.detail

import com.miquelcms.rickmorty.core.analytics.AnalyticsEvent
import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.core.testing.FakeAnalyticsTracker
import com.miquelcms.rickmorty.core.testing.MainDispatcherRule
import com.miquelcms.rickmorty.core.ui.toUiText
import com.miquelcms.rickmorty.feature.characters.domain.usecase.GetCharacterEpisodesUseCase
import com.miquelcms.rickmorty.feature.characters.ui.FakeCharacterRepository
import com.miquelcms.rickmorty.feature.characters.ui.FakeEpisodeRepository
import com.miquelcms.rickmorty.feature.characters.ui.character
import com.miquelcms.rickmorty.feature.characters.ui.episode
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterDetailUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import com.miquelcms.rickmorty.feature.characters.ui.model.EpisodeUi
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class CharacterDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val characterRepository = FakeCharacterRepository()
    private val episodeRepository = FakeEpisodeRepository()
    private val analyticsTracker = FakeAnalyticsTracker()

    private val rick = character(id = 1, name = "Rick Sanchez").copy(
        type = "Scientist",
        episodeIds = listOf(1, 2),
    )
    private val rickUi = CharacterDetailUi(
        id = 1,
        name = "Rick Sanchez",
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        status = CharacterStatusUi.ALIVE,
        species = "Human",
        type = "Scientist",
        gender = CharacterGenderUi.MALE,
        originName = "Earth (C-137)",
        locationName = "Citadel of Ricks",
    )
    private val episodesUi = listOf(
        EpisodeUi(id = 1, name = "Episode 1", code = "S01E01"),
        EpisodeUi(id = 2, name = "Episode 2", code = "S01E02"),
    )

    @Test
    fun `loads the character and its episodes when it starts`() {
        characterRepository.characterResult = Result.Success(rick)
        episodeRepository.episodesResult = Result.Success(listOf(episode(1), episode(2)))

        val viewModel = createViewModel()

        assertEquals(
            CharacterDetailState(character = rickUi, episodes = episodesUi, isLoading = false),
            viewModel.state.value,
        )
        assertEquals(setOf(1), characterRepository.requestedCharacterIds.toSet())
        assertEquals(listOf(listOf(1, 2)), episodeRepository.requestedIds)
    }

    @Test
    fun `shows the character while its episodes are still loading`() {
        val pause = CompletableDeferred<Unit>()
        characterRepository.characterResult = Result.Success(rick)
        episodeRepository.episodesResult = Result.Success(listOf(episode(1), episode(2)))
        episodeRepository.pause = pause

        val viewModel = createViewModel()

        assertEquals(
            CharacterDetailState(character = rickUi, isLoading = false, isLoadingEpisodes = true),
            viewModel.state.value,
        )
        pause.complete(Unit)
        assertEquals(
            CharacterDetailState(character = rickUi, episodes = episodesUi, isLoading = false),
            viewModel.state.value,
        )
    }

    @Test
    fun `hides the type when the character has none`() {
        characterRepository.characterResult = Result.Success(rick.copy(type = ""))

        val viewModel = createViewModel()

        assertNull(viewModel.state.value.character?.type)
        assertEquals("Rick Sanchez", viewModel.state.value.character?.name)
    }

    @Test
    fun `tracks the screen view and the opened character when it starts`() {
        createViewModel(characterId = 7)

        assertEquals(
            listOf(
                AnalyticsEvent("screen_view", mapOf("screen_name" to "character_detail")),
                AnalyticsEvent("character_opened", mapOf("character_id" to "7")),
            ),
            analyticsTracker.events,
        )
    }

    @Test
    fun `shows an error when the character fails`() {
        characterRepository.characterResult = Result.Failure(DataError.Network.NO_INTERNET)

        val viewModel = createViewModel()

        assertEquals(
            CharacterDetailState(
                isLoading = false,
                error = DataError.Network.NO_INTERNET.toUiText(),
            ),
            viewModel.state.value,
        )
        assertEquals(emptyList<List<Int>>(), episodeRepository.requestedIds)
    }

    @Test
    fun `retry loads the character and its episodes after an error`() {
        characterRepository.characterResult = Result.Failure(DataError.Network.NO_INTERNET)
        val viewModel = createViewModel()
        characterRepository.characterResult = Result.Success(rick)
        episodeRepository.episodesResult = Result.Success(listOf(episode(1), episode(2)))

        viewModel.onAction(CharacterDetailAction.OnRetryClick)

        assertEquals(
            CharacterDetailState(character = rickUi, episodes = episodesUi, isLoading = false),
            viewModel.state.value,
        )
    }

    @Test
    fun `keeps the character when its episodes fail`() {
        characterRepository.characterResult = Result.Success(rick)
        episodeRepository.episodesResult = Result.Failure(DataError.Network.SERVER_ERROR)

        val viewModel = createViewModel()

        assertEquals(
            CharacterDetailState(
                character = rickUi,
                isLoading = false,
                episodesError = DataError.Network.SERVER_ERROR.toUiText(),
            ),
            viewModel.state.value,
        )
    }

    @Test
    fun `retry loads only the episodes when they are what failed`() {
        characterRepository.characterResult = Result.Success(rick)
        episodeRepository.episodesResult = Result.Failure(DataError.Network.SERVER_ERROR)
        val viewModel = createViewModel()
        val pause = CompletableDeferred<Unit>()
        episodeRepository.episodesResult = Result.Success(listOf(episode(1), episode(2)))
        characterRepository.pause = pause

        viewModel.onAction(CharacterDetailAction.OnRetryClick)

        assertEquals(
            CharacterDetailState(character = rickUi, isLoading = false, isLoadingEpisodes = true),
            viewModel.state.value,
        )
        pause.complete(Unit)
        assertEquals(
            CharacterDetailState(character = rickUi, episodes = episodesUi, isLoading = false),
            viewModel.state.value,
        )
        assertEquals(2, episodeRepository.requestedIds.size)
    }

    private fun createViewModel(characterId: Int = 1) = CharacterDetailViewModel(
        characterId = characterId,
        characterRepository = characterRepository,
        getCharacterEpisodes = GetCharacterEpisodesUseCase(characterRepository, episodeRepository),
        analyticsTracker = analyticsTracker,
    )
}
