package com.miquelcms.rickmorty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.miquelcms.rickmorty.feature.characters.ui.detail.CharacterDetailRoot
import com.miquelcms.rickmorty.feature.characters.ui.list.CharacterListRoot

@Composable
fun RickMortyNavigation(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(CharacterListKey)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.goBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<CharacterListKey> {
                CharacterListRoot(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme,
                    onNavigateToDetail = { backStack.openCharacterDetail(it) },
                )
            }
            entry<CharacterDetailKey> { key ->
                CharacterDetailRoot(
                    characterId = key.characterId,
                    onBack = { backStack.goBack() },
                )
            }
        },
    )
}
