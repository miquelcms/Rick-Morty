package com.miquelcms.rickmorty.core.analytics.di

import com.miquelcms.rickmorty.core.analytics.AnalyticsTracker
import com.miquelcms.rickmorty.core.analytics.ErrorReporter
import com.miquelcms.rickmorty.core.analytics.LogcatAnalyticsTracker
import com.miquelcms.rickmorty.core.analytics.LogcatErrorReporter
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreAnalyticsModule = module {
    singleOf(::LogcatAnalyticsTracker) { bind<AnalyticsTracker>() }
    singleOf(::LogcatErrorReporter) { bind<ErrorReporter>() }
}
