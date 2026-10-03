package com.miquelcms.rickmorty.feature.characters.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.component.RmBackButton
import com.miquelcms.rickmorty.core.designsystem.component.RmTopBar
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.feature.characters.ui.R

@Composable
fun CharacterDetailRoot(onBack: () -> Unit) {
    CharacterDetailScreen(onBackClick = onBack)
}

@Composable
fun CharacterDetailScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            RmTopBar(
                title = stringResource(R.string.character_detail_title),
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
        CharacterDetailScreen(onBackClick = {})
    }
}
