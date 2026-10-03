package com.miquelcms.rickmorty.feature.characters.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miquelcms.rickmorty.core.designsystem.component.RmBackButton
import com.miquelcms.rickmorty.core.designsystem.component.RmImage
import com.miquelcms.rickmorty.core.designsystem.component.RmMessage
import com.miquelcms.rickmorty.core.designsystem.component.RmOutlinedButton
import com.miquelcms.rickmorty.core.designsystem.component.RmSkeleton
import com.miquelcms.rickmorty.core.designsystem.component.RmTopBar
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.designsystem.theme.Spacing
import com.miquelcms.rickmorty.core.ui.UiText
import com.miquelcms.rickmorty.core.ui.asString
import com.miquelcms.rickmorty.feature.characters.ui.R
import com.miquelcms.rickmorty.feature.characters.ui.detail.component.CharacterInfo
import com.miquelcms.rickmorty.feature.characters.ui.detail.component.EpisodeItem
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterDetailUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import com.miquelcms.rickmorty.feature.characters.ui.model.EpisodeUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private val SkeletonTitleHeight = 28.dp
private val SkeletonTextHeight = 14.dp
private const val SKELETON_EPISODE_COUNT = 4

@Composable
fun CharacterDetailRoot(
    characterId: Int,
    onBack: () -> Unit,
    viewModel: CharacterDetailViewModel = koinViewModel { parametersOf(characterId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBack,
    )
}

@Composable
fun CharacterDetailScreen(
    state: CharacterDetailState,
    onAction: (CharacterDetailAction) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            RmTopBar(
                title = stringResource(R.string.character_detail_title),
                navigationIcon = { RmBackButton(onClick = onBackClick) },
            )
        },
    ) { innerPadding ->
        when {
            state.isLoading -> CharacterDetailSkeleton(contentPadding = innerPadding)

            state.error != null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                RmMessage(
                    title = stringResource(R.string.character_detail_error_title),
                    description = state.error.asString(),
                    actionText = stringResource(R.string.action_retry),
                    onActionClick = { onAction(CharacterDetailAction.OnRetryClick) },
                )
            }

            state.character != null -> CharacterDetailContent(
                character = state.character,
                episodes = state.episodes,
                isLoadingEpisodes = state.isLoadingEpisodes,
                episodesError = state.episodesError?.asString(),
                onRetryClick = { onAction(CharacterDetailAction.OnRetryClick) },
                contentPadding = innerPadding,
            )
        }
    }
}

@Composable
private fun CharacterDetailContent(
    character: CharacterDetailUi,
    episodes: List<EpisodeUi>,
    isLoadingEpisodes: Boolean,
    episodesError: String?,
    onRetryClick: () -> Unit,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        item(contentType = "image") {
            RmImage(
                url = character.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        }
        item(contentType = "info") {
            CharacterInfo(character = character)
        }
        item(contentType = "episodesTitle") {
            Text(
                text = stringResource(R.string.character_detail_episodes).uppercase(),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = Spacing.md, vertical = Spacing.sm)
                    .semantics { heading() },
            )
        }
        when {
            isLoadingEpisodes -> items(count = SKELETON_EPISODE_COUNT) {
                RmSkeleton(
                    modifier = Modifier
                        .padding(horizontal = Spacing.md, vertical = Spacing.sm)
                        .fillMaxWidth(fraction = 0.6f)
                        .height(SkeletonTextHeight),
                )
            }

            episodesError != null -> item(contentType = "episodesError") {
                EpisodesError(message = episodesError, onRetryClick = onRetryClick)
            }

            else -> items(items = episodes, key = { it.id }, contentType = { "episode" }) {
                EpisodeItem(episode = it)
            }
        }
    }
}

@Composable
private fun EpisodesError(
    message: String,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        RmOutlinedButton(
            text = stringResource(R.string.action_retry),
            onClick = onRetryClick,
        )
    }
}

@Composable
private fun CharacterDetailSkeleton(contentPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
    ) {
        RmSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        )
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            RmSkeleton(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.7f)
                    .height(SkeletonTitleHeight),
            )
            RmSkeleton(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.4f)
                    .height(SkeletonTextHeight),
            )
            RmSkeleton(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .height(SkeletonTextHeight),
            )
        }
    }
}

private val previewCharacter = CharacterDetailUi(
    id = 1,
    name = "Rick Sanchez",
    imageUrl = "",
    status = CharacterStatusUi.ALIVE,
    species = "Human",
    type = null,
    gender = CharacterGenderUi.MALE,
    originName = "Earth (C-137)",
    locationName = "Citadel of Ricks",
)

@PreviewLightDark
@Composable
private fun CharacterDetailScreenPreview() {
    RickMortyTheme {
        CharacterDetailScreen(
            state = CharacterDetailState(
                character = previewCharacter,
                episodes = listOf(
                    EpisodeUi(id = 1, name = "Pilot", code = "S01E01"),
                    EpisodeUi(id = 2, name = "Lawnmower Dog", code = "S01E02"),
                ),
                isLoading = false,
            ),
            onAction = {},
            onBackClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun CharacterDetailScreenLoadingPreview() {
    RickMortyTheme {
        CharacterDetailScreen(
            state = CharacterDetailState(),
            onAction = {},
            onBackClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun CharacterDetailScreenErrorPreview() {
    RickMortyTheme {
        CharacterDetailScreen(
            state = CharacterDetailState(
                isLoading = false,
                error = UiText.DynamicString("No internet connection. Check it and try again."),
            ),
            onAction = {},
            onBackClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun CharacterDetailScreenEpisodesLoadingPreview() {
    RickMortyTheme {
        CharacterDetailScreen(
            state = CharacterDetailState(
                character = previewCharacter,
                isLoading = false,
                isLoadingEpisodes = true,
            ),
            onAction = {},
            onBackClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun CharacterDetailScreenEpisodesErrorPreview() {
    RickMortyTheme {
        CharacterDetailScreen(
            state = CharacterDetailState(
                character = previewCharacter,
                isLoading = false,
                episodesError = UiText.DynamicString("The server is having problems."),
            ),
            onAction = {},
            onBackClick = {},
        )
    }
}
