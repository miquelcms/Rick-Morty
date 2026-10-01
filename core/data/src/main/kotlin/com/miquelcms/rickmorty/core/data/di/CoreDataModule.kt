package com.miquelcms.rickmorty.core.data.di

import com.miquelcms.rickmorty.core.data.network.createJson
import com.miquelcms.rickmorty.core.data.network.createOkHttpClient
import com.miquelcms.rickmorty.core.data.network.createRetrofit
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    single { createJson() }
    single { createOkHttpClient(androidContext().cacheDir) }
    single { createRetrofit(get(), get()) }
}
