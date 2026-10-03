package com.miquelcms.rickmorty.feature.characters.data.di

import com.miquelcms.rickmorty.feature.characters.data.remote.api.CharacterApi
import com.miquelcms.rickmorty.feature.characters.data.remote.api.EpisodeApi
import com.miquelcms.rickmorty.feature.characters.data.repository.NetworkCharacterRepository
import com.miquelcms.rickmorty.feature.characters.data.repository.NetworkEpisodeRepository
import com.miquelcms.rickmorty.feature.characters.domain.repository.CharacterRepository
import com.miquelcms.rickmorty.feature.characters.domain.repository.EpisodeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val charactersDataModule = module {
    single { get<Retrofit>().create<CharacterApi>() }
    single { get<Retrofit>().create<EpisodeApi>() }
    singleOf(::NetworkCharacterRepository) { bind<CharacterRepository>() }
    singleOf(::NetworkEpisodeRepository) { bind<EpisodeRepository>() }
}
