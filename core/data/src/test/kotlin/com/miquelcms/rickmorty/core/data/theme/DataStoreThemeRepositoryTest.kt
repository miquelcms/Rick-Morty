package com.miquelcms.rickmorty.core.data.theme

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.miquelcms.rickmorty.core.domain.theme.ThemeMode
import com.miquelcms.rickmorty.core.testing.FakeErrorReporter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class DataStoreThemeRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `theme mode is SYSTEM when nothing was stored`() = runTest {
        val repository = createRepository()

        assertEquals(ThemeMode.SYSTEM, repository.themeMode.first())
    }

    @Test
    fun `returns the stored theme mode`() = runTest {
        val repository = createRepository()

        repository.setThemeMode(ThemeMode.DARK)

        assertEquals(ThemeMode.DARK, repository.themeMode.first())
    }

    private fun TestScope.createRepository() = DataStoreThemeRepository(
        dataStore = PreferenceDataStoreFactory.create(scope = backgroundScope) {
            File(temporaryFolder.root, "settings.preferences_pb")
        },
        errorReporter = FakeErrorReporter(),
    )
}
