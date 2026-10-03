package com.miquelcms.rickmorty

import android.app.Application
import com.miquelcms.rickmorty.core.analytics.di.coreAnalyticsModule
import com.miquelcms.rickmorty.core.data.di.coreDataModule
import com.miquelcms.rickmorty.di.appModule
import com.miquelcms.rickmorty.feature.characters.data.di.charactersDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RickMortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RickMortyApplication)
            modules(
                appModule,
                coreAnalyticsModule,
                coreDataModule,
                charactersDataModule,
            )
        }
    }
}
