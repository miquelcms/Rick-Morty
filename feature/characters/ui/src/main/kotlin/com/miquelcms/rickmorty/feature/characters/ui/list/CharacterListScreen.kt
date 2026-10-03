package com.miquelcms.rickmorty.feature.characters.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miquelcms.rickmorty.core.designsystem.component.RmMessage
import com.miquelcms.rickmorty.core.designsystem.component.RmPullToRefreshBox
import com.miquelcms.rickmorty.core.designsystem.component.RmSearchField
import com.miquelcms.rickmorty.core.designsystem.component.RmThemeToggleButton
import com.miquelcms.rickmorty.core.designsystem.component.RmTopBar
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.ui.ObserveAsEvents
import com.miquelcms.rickmorty.core.ui.UiText
import com.miquelcms.rickmorty.core.ui.asString
import com.miquelcms.rickmorty.feature.characters.ui.R
import com.miquelcms.rickmorty.feature.characters.ui.list.component.CharacterGrid
import com.miquelcms.rickmorty.feature.characters.ui.list.component.CharacterGridSkeleton
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterUi
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterListRoot(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: CharacterListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CharacterListEvent.NavigateToDetail -> onNavigateToDetail(event.characterId)
            is CharacterListEvent.ShowMessage -> coroutineScope.launch {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    CharacterListScreen(
        state = state,
        onAction = viewModel::onAction,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
fun CharacterListScreen(
    state: CharacterListState,
    onAction: (CharacterListAction) -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        topBar = {
            Column {
                RmTopBar(
                    title = stringResource(R.string.character_list_title),
                    actions = {
                        RmThemeToggleButton(isDarkTheme = isDarkTheme, onClick = onToggleTheme)
                    },
                )
                RmSearchField(
                    query = state.query,
                    onQueryChange = { onAction(CharacterListAction.OnQueryChange(it)) },
                    placeholder = stringResource(R.string.character_list_search_placeholder),
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        RmPullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(CharacterListAction.OnRefresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                state.isLoading -> CharacterGridSkeleton()

                state.error != null -> CenteredMessage {
                    RmMessage(
                        title = stringResource(R.string.character_list_error_title),
                        description = state.error.asString(),
                        actionText = stringResource(R.string.action_retry),
                        onActionClick = { onAction(CharacterListAction.OnRetryClick) },
                    )
                }

                state.characters.isEmpty() -> CenteredMessage {
                    RmMessage(
                        title = stringResource(R.string.character_list_empty_title),
                        description = stringResource(R.string.character_list_empty_description),
                        actionText = stringResource(R.string.character_list_clear_search),
                        onActionClick = { onAction(CharacterListAction.OnClearSearchClick) },
                    )
                }

                else -> CharacterGrid(
                    characters = state.characters,
                    isLoadingNextPage = state.isLoadingNextPage,
                    nextPageError = state.nextPageError?.asString(),
                    onCharacterClick = { onAction(CharacterListAction.OnCharacterClick(it)) },
                    onLoadNextPage = { onAction(CharacterListAction.OnLoadNextPage) },
                    onRetryClick = { onAction(CharacterListAction.OnRetryClick) },
                )
            }
        }
    }
}

@Composable
private fun CenteredMessage(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@PreviewLightDark
@Composable
private fun CharacterListScreenPreview() {
    RickMortyTheme {
        CharacterListScreen(
            state = CharacterListState(
                characters = listOf(
                    CharacterUi(1, "Rick Sanchez", "", CharacterStatusUi.ALIVE),
                    CharacterUi(2, "Morty Smith", "", CharacterStatusUi.ALIVE),
                    CharacterUi(3, "Birdperson", "", CharacterStatusUi.DEAD),
                    CharacterUi(4, "Abradolf Lincler", "", CharacterStatusUi.UNKNOWN),
                ),
                isLoading = false,
            ),
            onAction = {},
            isDarkTheme = false,
            onToggleTheme = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun CharacterListScreenLoadingPreview() {
    RickMortyTheme {
        CharacterListScreen(
            state = CharacterListState(isLoading = true),
            onAction = {},
            isDarkTheme = false,
            onToggleTheme = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun CharacterListScreenErrorPreview() {
    RickMortyTheme {
        CharacterListScreen(
            state = CharacterListState(
                isLoading = false,
                error = UiText.DynamicString("No internet connection. Check it and try again."),
            ),
            onAction = {},
            isDarkTheme = false,
            onToggleTheme = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun CharacterListScreenEmptyPreview() {
    RickMortyTheme {
        CharacterListScreen(
            state = CharacterListState(query = "zzz", isLoading = false),
            onAction = {},
            isDarkTheme = false,
            onToggleTheme = {},
        )
    }
}
