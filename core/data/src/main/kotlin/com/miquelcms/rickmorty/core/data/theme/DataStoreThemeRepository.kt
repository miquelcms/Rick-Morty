package com.miquelcms.rickmorty.core.data.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.miquelcms.rickmorty.core.analytics.ErrorReporter
import com.miquelcms.rickmorty.core.domain.theme.ThemeMode
import com.miquelcms.rickmorty.core.domain.theme.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

internal class DataStoreThemeRepository(
    private val dataStore: DataStore<Preferences>,
    private val errorReporter: ErrorReporter,
) : ThemeRepository {

    override val themeMode: Flow<ThemeMode> = dataStore.data
        .catch { error ->
            if (error !is IOException) throw error
            errorReporter.report(error)
            emit(emptyPreferences())
        }
        .map { preferences ->
            val storedName = preferences[THEME_MODE_KEY]
            ThemeMode.entries.find { it.name == storedName } ?: ThemeMode.SYSTEM
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        try {
            dataStore.edit { preferences -> preferences[THEME_MODE_KEY] = themeMode.name }
        } catch (e: IOException) {
            errorReporter.report(e)
        }
    }

    private companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }
}
