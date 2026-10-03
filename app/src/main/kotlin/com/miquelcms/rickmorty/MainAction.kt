package com.miquelcms.rickmorty

sealed interface MainAction {
    data class OnDarkThemeChange(val isDarkTheme: Boolean) : MainAction
}
