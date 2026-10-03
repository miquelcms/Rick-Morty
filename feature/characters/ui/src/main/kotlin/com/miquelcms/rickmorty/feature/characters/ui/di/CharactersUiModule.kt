package com.miquelcms.rickmorty.feature.characters.ui.di

import com.miquelcms.rickmorty.feature.characters.domain.usecase.GetCharacterEpisodesUseCase
import com.miquelcms.rickmorty.feature.characters.ui.detail.CharacterDetailViewModel
import com.miquelcms.rickmorty.feature.characters.ui.list.CharacterListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val charactersUiModule = module {
    factoryOf(::GetCharacterEpisodesUseCase)
    viewModelOf(::CharacterListViewModel)
    viewModelOf(::CharacterDetailViewModel)
}
