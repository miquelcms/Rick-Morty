package com.miquelcms.rickmorty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miquelcms.rickmorty.core.domain.theme.ThemeMode
import com.miquelcms.rickmorty.core.domain.theme.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val themeRepository: ThemeRepository,
) : ViewModel() {

    val state: StateFlow<MainState> = themeRepository.themeMode
        .map { themeMode -> MainState(isLoading = false, themeMode = themeMode) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainState(),
        )

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.OnDarkThemeChange -> changeDarkTheme(action.isDarkTheme)
        }
    }

    private fun changeDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            themeRepository.setThemeMode(if (isDarkTheme) ThemeMode.DARK else ThemeMode.LIGHT)
        }
    }
}
