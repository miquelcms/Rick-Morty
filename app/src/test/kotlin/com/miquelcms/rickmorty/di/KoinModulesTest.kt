package com.miquelcms.rickmorty.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.miquelcms.rickmorty.core.analytics.di.coreAnalyticsModule
import com.miquelcms.rickmorty.core.data.di.coreDataModule
import com.miquelcms.rickmorty.feature.characters.data.di.charactersDataModule
import com.miquelcms.rickmorty.feature.characters.ui.di.charactersUiModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class KoinModulesTest {

    @Test
    fun `every dependency has a definition`() {
        val allModules = module {
            includes(
                appModule,
                coreAnalyticsModule,
                coreDataModule,
                charactersDataModule,
                charactersUiModule,
            )
        }

        allModules.verify(extraTypes = listOf(Context::class, SavedStateHandle::class))
    }
}
