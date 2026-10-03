package com.miquelcms.rickmorty.feature.characters.ui.di

import com.miquelcms.rickmorty.feature.characters.ui.list.CharacterListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val charactersUiModule = module {
    viewModelOf(::CharacterListViewModel)
}
