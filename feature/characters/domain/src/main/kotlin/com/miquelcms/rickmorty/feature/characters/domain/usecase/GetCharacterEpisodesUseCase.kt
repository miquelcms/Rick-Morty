package com.miquelcms.rickmorty.feature.characters.domain.usecase

import com.miquelcms.rickmorty.core.domain.DataError
import com.miquelcms.rickmorty.core.domain.Result
import com.miquelcms.rickmorty.feature.characters.domain.model.Episode
import com.miquelcms.rickmorty.feature.characters.domain.repository.CharacterRepository
import com.miquelcms.rickmorty.feature.characters.domain.repository.EpisodeRepository

class GetCharacterEpisodesUseCase(
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository,
) {

    suspend operator fun invoke(characterId: Int): Result<List<Episode>, DataError> =
        when (val character = characterRepository.getCharacter(characterId)) {
            is Result.Success -> episodeRepository.getEpisodes(character.data.episodeIds)
            is Result.Failure -> character
        }
}
