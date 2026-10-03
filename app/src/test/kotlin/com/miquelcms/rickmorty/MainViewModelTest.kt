package com.miquelcms.rickmorty

import com.miquelcms.rickmorty.core.domain.theme.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val themeRepository = FakeThemeRepository(initialThemeMode = ThemeMode.DARK)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = MainViewModel(themeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state starts as loading`() {
        assertEquals(MainState(isLoading = true), viewModel.state.value)
    }

    @Test
    fun `state contains the stored theme mode`() = runTest {
        collectState()

        assertEquals(MainState(isLoading = false, themeMode = ThemeMode.DARK), viewModel.state.value)
    }

    @Test
    fun `turning dark theme off stores LIGHT`() = runTest {
        collectState()

        viewModel.onAction(MainAction.OnDarkThemeChange(isDarkTheme = false))

        assertEquals(MainState(isLoading = false, themeMode = ThemeMode.LIGHT), viewModel.state.value)
    }

    @Test
    fun `turning dark theme on stores DARK`() = runTest {
        themeRepository.setThemeMode(ThemeMode.LIGHT)

        viewModel.onAction(MainAction.OnDarkThemeChange(isDarkTheme = true))

        assertEquals(ThemeMode.DARK, themeRepository.themeMode.value)
    }

    private fun TestScope.collectState() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
    }
}
