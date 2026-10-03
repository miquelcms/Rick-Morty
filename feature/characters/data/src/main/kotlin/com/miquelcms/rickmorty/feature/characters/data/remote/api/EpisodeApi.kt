package com.miquelcms.rickmorty.feature.characters.data.remote.api

import com.miquelcms.rickmorty.feature.characters.data.remote.dto.EpisodeDto
import retrofit2.http.GET
import retrofit2.http.Path

internal interface EpisodeApi {

    @GET("episode/{ids}")
    suspend fun getEpisodes(@Path("ids") ids: String): List<EpisodeDto>
}
