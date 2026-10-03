package com.miquelcms.rickmorty.core.data.di

import com.miquelcms.rickmorty.core.data.BuildConfig
import com.miquelcms.rickmorty.core.data.network.createJson
import com.miquelcms.rickmorty.core.data.network.createOkHttpClient
import com.miquelcms.rickmorty.core.data.network.createRetrofit
import com.miquelcms.rickmorty.core.data.theme.DataStoreThemeRepository
import com.miquelcms.rickmorty.core.data.theme.createPreferencesDataStore
import com.miquelcms.rickmorty.core.domain.theme.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreDataModule = module {
    single { createJson() }
    single { createOkHttpClient(androidContext().cacheDir, loggingEnabled = BuildConfig.DEBUG) }
    single { createRetrofit(get(), get()) }
    single { createPreferencesDataStore(androidContext()) }
    singleOf(::DataStoreThemeRepository) { bind<ThemeRepository>() }
}
