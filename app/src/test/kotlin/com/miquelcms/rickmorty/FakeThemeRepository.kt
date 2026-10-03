package com.miquelcms.rickmorty

import com.miquelcms.rickmorty.core.domain.theme.ThemeMode
import com.miquelcms.rickmorty.core.domain.theme.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow

class FakeThemeRepository(initialThemeMode: ThemeMode = ThemeMode.SYSTEM) : ThemeRepository {

    override val themeMode = MutableStateFlow(initialThemeMode)

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        this.themeMode.value = themeMode
    }
}
