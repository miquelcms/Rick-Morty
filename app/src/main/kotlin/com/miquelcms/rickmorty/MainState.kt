package com.miquelcms.rickmorty

import com.miquelcms.rickmorty.core.domain.theme.ThemeMode

data class MainState(
    val isLoading: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)
