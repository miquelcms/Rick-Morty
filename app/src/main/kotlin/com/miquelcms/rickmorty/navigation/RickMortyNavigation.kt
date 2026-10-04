package com.miquelcms.rickmorty.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.miquelcms.rickmorty.core.designsystem.component.LocalAnimatedVisibilityScope
import com.miquelcms.rickmorty.core.designsystem.component.LocalSharedTransitionScope
import com.miquelcms.rickmorty.feature.characters.ui.detail.CharacterDetailRoot
import com.miquelcms.rickmorty.feature.characters.ui.list.CharacterListRoot

@Composable
fun RickMortyNavigation(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(CharacterListKey)

    SharedTransitionLayout(modifier = modifier) {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.goBack() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                sharedTransitionScope = this,
                entryProvider = entryProvider {
                    entry<CharacterListKey> {
                        WithAnimatedVisibilityScope {
                            CharacterListRoot(
                                isDarkTheme = isDarkTheme,
                                onToggleTheme = onToggleTheme,
                                onNavigateToDetail = { backStack.openCharacterDetail(it) },
                            )
                        }
                    }
                    entry<CharacterDetailKey> { key ->
                        WithAnimatedVisibilityScope {
                            CharacterDetailRoot(
                                characterId = key.characterId,
                                onBack = { backStack.goBack() },
                            )
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun WithAnimatedVisibilityScope(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAnimatedVisibilityScope provides LocalNavAnimatedContentScope.current,
        content = content,
    )
}
