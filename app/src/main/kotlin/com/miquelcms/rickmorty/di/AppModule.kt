package com.miquelcms.rickmorty.di

import com.miquelcms.rickmorty.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
}
