package com.miquelcms.rickmorty.feature.characters.ui.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miquelcms.rickmorty.core.designsystem.component.RmLoadingIndicator
import com.miquelcms.rickmorty.core.designsystem.component.RmOutlinedButton
import com.miquelcms.rickmorty.core.designsystem.component.RmSkeleton
import com.miquelcms.rickmorty.core.designsystem.theme.Spacing
import com.miquelcms.rickmorty.feature.characters.ui.R
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterUi

private val CardMinWidth = 160.dp
private val SkeletonTextHeight = 14.dp
private const val LOAD_NEXT_PAGE_THRESHOLD = 6
private const val SKELETON_CARD_COUNT = 8

@Composable
internal fun CharacterGrid(
    characters: List<CharacterUi>,
    isLoadingNextPage: Boolean,
    nextPageError: String?,
    onCharacterClick: (Int) -> Unit,
    onLoadNextPage: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    val isNearEnd by remember {
        derivedStateOf {
            val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= gridState.layoutInfo.totalItemsCount - LOAD_NEXT_PAGE_THRESHOLD
        }
    }
    LaunchedEffect(isNearEnd, characters.size) {
        if (isNearEnd) onLoadNextPage()
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = CardMinWidth),
        modifier = modifier,
        state = gridState,
        contentPadding = PaddingValues(bottom = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        items(items = characters, key = { it.id }, contentType = { "character" }) { character ->
            CharacterCard(character = character, onClick = { onCharacterClick(character.id) })
        }
        if (isLoadingNextPage || nextPageError != null) {
            item(span = { GridItemSpan(maxLineSpan) }, contentType = "footer") {
                NextPageFooter(error = nextPageError, onRetryClick = onRetryClick)
            }
        }
    }
}

@Composable
internal fun CharacterGridSkeleton(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = CardMinWidth),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
        userScrollEnabled = false,
    ) {
        items(count = SKELETON_CARD_COUNT) {
            Column {
                RmSkeleton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
                Column(
                    modifier = Modifier.padding(Spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    RmSkeleton(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.7f)
                            .height(SkeletonTextHeight),
                    )
                    RmSkeleton(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.4f)
                            .height(SkeletonTextHeight),
                    )
                }
            }
        }
    }
}

@Composable
private fun NextPageFooter(
    error: String?,
    onRetryClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        if (error == null) {
            RmLoadingIndicator()
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = error,
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
    }
}
