package com.miquelcms.rickmorty.feature.characters.ui.list

import androidx.lifecycle.SavedStateHandle
import com.miquelcms.rickmorty.core.analytics.AnalyticsEvent
import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.core.testing.FakeAnalyticsTracker
import com.miquelcms.rickmorty.core.testing.FakeNetworkMonitor
import com.miquelcms.rickmorty.core.testing.MainDispatcherRule
import com.miquelcms.rickmorty.core.ui.toUiText
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterFilters
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterGender
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterPage
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterSpecies
import com.miquelcms.rickmorty.feature.characters.domain.model.CharacterStatus
import com.miquelcms.rickmorty.feature.characters.ui.FakeCharacterRepository
import com.miquelcms.rickmorty.feature.characters.ui.FakeCharacterRepository.PageRequest
import com.miquelcms.rickmorty.feature.characters.ui.character
import com.miquelcms.rickmorty.feature.characters.ui.characterUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterFiltersUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterSpeciesUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCharacterRepository()
    private val networkMonitor = FakeNetworkMonitor()
    private val analyticsTracker = FakeAnalyticsTracker()

    private val firstPage = CharacterPage(listOf(character(1), character(2)), nextPage = 2)
    private val secondPage = CharacterPage(listOf(character(3)), nextPage = null)

    @Test
    fun `loads the first page when it starts`() {
        repository.firstPageResult = Result.Success(firstPage)

        val viewModel = createViewModel()

        assertEquals(
            CharacterListState(
                characters = listOf(characterUi(1), characterUi(2)),
                isLoading = false,
            ),
            viewModel.state.value,
        )
        assertEquals(listOf(PageRequest(null, CharacterFilters(), false)), repository.pageRequests)
    }

    @Test
    fun `tracks the screen view when it starts`() {
        createViewModel()

        val expected = AnalyticsEvent("screen_view", mapOf("screen_name" to "character_list"))
        assertEquals(listOf(expected), analyticsTracker.events)
    }

    @Test
    fun `shows an error when the first page fails`() {
        repository.firstPageResult = Result.Failure(DataError.Network.NO_INTERNET)

        val viewModel = createViewModel()

        assertEquals(
            CharacterListState(isLoading = false, error = DataError.Network.NO_INTERNET.toUiText()),
            viewModel.state.value,
        )
    }

    @Test
    fun `retry loads the first page again after an error`() {
        repository.firstPageResult = Result.Failure(DataError.Network.NO_INTERNET)
        val viewModel = createViewModel()
        repository.firstPageResult = Result.Success(firstPage)

        viewModel.onAction(CharacterListAction.OnRetryClick)

        assertEquals(listOf(characterUi(1), characterUi(2)), viewModel.state.value.characters)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `loading the next page adds its characters to the list`() {
        repository.firstPageResult = Result.Success(firstPage)
        repository.nextPageResults[2] = Result.Success(secondPage)
        val viewModel = createViewModel()

        viewModel.onAction(CharacterListAction.OnLoadNextPage)

        assertEquals(
            listOf(characterUi(1), characterUi(2), characterUi(3)),
            viewModel.state.value.characters,
        )
        assertEquals(listOf(null, 2), repository.pageRequests.map { it.page })
    }

    @Test
    fun `requests the page that the repository says is next`() {
        repository.firstPageResult = Result.Success(firstPage.copy(nextPage = 3))
        repository.nextPageResults[3] = Result.Success(secondPage)
        val viewModel = createViewModel()

        viewModel.onAction(CharacterListAction.OnLoadNextPage)

        assertEquals(listOf(null, 3), repository.pageRequests.map { it.page })
    }

    @Test
    fun `does not request more pages after the last one`() {
        repository.firstPageResult = Result.Success(firstPage)
        repository.nextPageResults[2] = Result.Success(secondPage)
        val viewModel = createViewModel()
        viewModel.onAction(CharacterListAction.OnLoadNextPage)

        viewModel.onAction(CharacterListAction.OnLoadNextPage)

        assertEquals(listOf(null, 2), repository.pageRequests.map { it.page })
    }

    @Test
    fun `a failed next page keeps the list and waits for a retry`() {
        repository.firstPageResult = Result.Success(firstPage)
        repository.nextPageResults[2] = Result.Failure(DataError.Network.SERVER_ERROR)
        val viewModel = createViewModel()

        viewModel.onAction(CharacterListAction.OnLoadNextPage)
        viewModel.onAction(CharacterListAction.OnLoadNextPage)

        assertEquals(listOf(characterUi(1), characterUi(2)), viewModel.state.value.characters)
        assertEquals(
            DataError.Network.SERVER_ERROR.toUiText(),
            viewModel.state.value.nextPageError,
        )
        assertEquals(listOf(null, 2), repository.pageRequests.map { it.page })
    }

    @Test
    fun `retry loads the failed next page`() {
        repository.firstPageResult = Result.Success(firstPage)
        repository.nextPageResults[2] = Result.Failure(DataError.Network.SERVER_ERROR)
        val viewModel = createViewModel()
        viewModel.onAction(CharacterListAction.OnLoadNextPage)
        repository.nextPageResults[2] = Result.Success(secondPage)

        viewModel.onAction(CharacterListAction.OnRetryClick)

        assertEquals(
            listOf(characterUi(1), characterUi(2), characterUi(3)),
            viewModel.state.value.characters,
        )
        assertNull(viewModel.state.value.nextPageError)
    }

    @Test
    fun `searches by name only after the user stops typing`() = runTest {
        repository.firstPageResult = Result.Success(firstPage)
        val viewModel = createViewModel()

        viewModel.onAction(CharacterListAction.OnQueryChange("ri"))
        advanceTimeBy(300)
        viewModel.onAction(CharacterListAction.OnQueryChange("rick"))
        advanceTimeBy(399)
        assertEquals(1, repository.pageRequests.size)
        advanceTimeBy(2)

        val expected = PageRequest(null, CharacterFilters(name = "rick"), false)
        assertEquals(expected, repository.pageRequests.last())
        assertEquals(2, repository.pageRequests.size)
        assertEquals("rick", viewModel.state.value.query)
        assertEquals(
            AnalyticsEvent("search", mapOf("query" to "rick")),
            analyticsTracker.events.last(),
        )
    }

    @Test
    fun `applying filters reloads the first page with them`() {
        repository.firstPageResult = Result.Success(firstPage)
        val viewModel = createViewModel()
        val filters = CharacterFiltersUi(
            status = CharacterStatusUi.DEAD,
            gender = CharacterGenderUi.FEMALE,
            species = CharacterSpeciesUi.HUMAN,
        )

        viewModel.onAction(CharacterListAction.OnFiltersApply(filters))

        val expectedFilters = CharacterFilters(
            status = CharacterStatus.DEAD,
            gender = CharacterGender.FEMALE,
            species = CharacterSpecies.HUMAN,
        )
        assertEquals(PageRequest(null, expectedFilters, false), repository.pageRequests.last())
        assertEquals(filters, viewModel.state.value.filters)
        assertEquals(
            AnalyticsEvent(
                name = "filters_applied",
                params = mapOf("status" to "dead", "gender" to "female", "species" to "human"),
            ),
            analyticsTracker.events.last(),
        )
    }

    @Test
    fun `clearing the search removes the query and the filters`() {
        repository.firstPageResult = Result.Success(firstPage)
        val savedStateHandle = SavedStateHandle(mapOf("query" to "rick", "status" to "DEAD"))
        val viewModel = createViewModel(savedStateHandle)

        viewModel.onAction(CharacterListAction.OnClearSearchClick)

        assertEquals("", viewModel.state.value.query)
        assertEquals(CharacterFiltersUi(), viewModel.state.value.filters)
        assertEquals(PageRequest(null, CharacterFilters(), false), repository.pageRequests.last())
    }

    @Test
    fun `refreshing skips the cache and replaces the list`() {
        repository.firstPageResult = Result.Success(firstPage)
        repository.nextPageResults[2] = Result.Success(secondPage)
        val viewModel = createViewModel()
        viewModel.onAction(CharacterListAction.OnLoadNextPage)

        viewModel.onAction(CharacterListAction.OnRefresh)

        assertEquals(PageRequest(null, CharacterFilters(), true), repository.pageRequests.last())
        assertEquals(listOf(characterUi(1), characterUi(2)), viewModel.state.value.characters)
        assertEquals(false, viewModel.state.value.isRefreshing)
    }

    @Test
    fun `a failed refresh keeps the list and shows a message`() = runTest {
        repository.firstPageResult = Result.Success(firstPage)
        val viewModel = createViewModel()
        val events = collectEvents(viewModel)
        repository.firstPageResult = Result.Failure(DataError.Network.NO_INTERNET)

        viewModel.onAction(CharacterListAction.OnRefresh)

        assertEquals(listOf(characterUi(1), characterUi(2)), viewModel.state.value.characters)
        assertEquals(false, viewModel.state.value.isRefreshing)
        assertEquals(
            listOf(CharacterListEvent.ShowMessage(DataError.Network.NO_INTERNET.toUiText())),
            events,
        )
    }

    @Test
    fun `opening a character navigates to its detail and tracks it`() = runTest {
        repository.firstPageResult = Result.Success(firstPage)
        val viewModel = createViewModel()
        val events = collectEvents(viewModel)

        viewModel.onAction(CharacterListAction.OnCharacterClick(characterId = 2))

        assertEquals(listOf(CharacterListEvent.NavigateToDetail(characterId = 2)), events)
        assertEquals(
            AnalyticsEvent("character_opened", mapOf("character_id" to "2")),
            analyticsTracker.events.last(),
        )
    }

    @Test
    fun `shows the offline notice only while there is no connection`() {
        repository.firstPageResult = Result.Success(firstPage)
        val viewModel = createViewModel()
        assertEquals(false, viewModel.state.value.isOffline)

        networkMonitor.isOnline.value = false
        assertEquals(true, viewModel.state.value.isOffline)

        networkMonitor.isOnline.value = true
        assertEquals(false, viewModel.state.value.isOffline)
    }

    @Test
    fun `restores the query and the filters after process death`() {
        repository.firstPageResult = Result.Success(firstPage)
        val savedStateHandle = SavedStateHandle(
            mapOf("query" to "rick", "status" to "ALIVE", "gender" to "MALE", "species" to "ROBOT"),
        )

        val viewModel = createViewModel(savedStateHandle)

        val expectedFilters = CharacterFiltersUi(
            status = CharacterStatusUi.ALIVE,
            gender = CharacterGenderUi.MALE,
            species = CharacterSpeciesUi.ROBOT,
        )
        assertEquals("rick", viewModel.state.value.query)
        assertEquals(expectedFilters, viewModel.state.value.filters)
        assertEquals("rick", repository.pageRequests.single().filters.name)
    }

    private fun createViewModel(savedStateHandle: SavedStateHandle = SavedStateHandle()) =
        CharacterListViewModel(repository, networkMonitor, analyticsTracker, savedStateHandle)

    private fun TestScope.collectEvents(
        viewModel: CharacterListViewModel,
    ): List<CharacterListEvent> {
        val events = mutableListOf<CharacterListEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.toList(events)
        }
        return events
    }
}
