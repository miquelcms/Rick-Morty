package com.miquelcms.rickmorty.core.data.di

import com.miquelcms.rickmorty.core.data.BuildConfig
import com.miquelcms.rickmorty.core.data.network.createJson
import com.miquelcms.rickmorty.core.data.network.createOkHttpClient
import com.miquelcms.rickmorty.core.data.network.createRetrofit
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    single { createJson() }
    single { createOkHttpClient(androidContext().cacheDir, loggingEnabled = BuildConfig.DEBUG) }
    single { createRetrofit(get(), get()) }
}
