package com.miquelcms.rickmorty.feature.characters.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miquelcms.rickmorty.core.designsystem.component.RmBackButton
import com.miquelcms.rickmorty.core.designsystem.component.RmTopBar
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.feature.characters.ui.R
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterDetailUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CharacterDetailRoot(
    characterId: Int,
    onBack: () -> Unit,
    viewModel: CharacterDetailViewModel = koinViewModel { parametersOf(characterId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterDetailScreen(state = state, onBackClick = onBack)
}

@Composable
fun CharacterDetailScreen(
    state: CharacterDetailState,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            RmTopBar(
                title = state.character?.name ?: stringResource(R.string.character_detail_title),
                navigationIcon = { RmBackButton(onClick = onBackClick) },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@PreviewLightDark
@Composable
private fun CharacterDetailScreenPreview() {
    RickMortyTheme {
        CharacterDetailScreen(
            state = CharacterDetailState(
                character = CharacterDetailUi(
                    id = 1,
                    name = "Rick Sanchez",
                    imageUrl = "",
                    status = CharacterStatusUi.ALIVE,
                    species = "Human",
                    type = null,
                    gender = CharacterGenderUi.MALE,
                    originName = "Earth (C-137)",
                    locationName = "Citadel of Ricks",
                ),
                isLoading = false,
            ),
            onBackClick = {},
        )
    }
}
