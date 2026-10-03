package com.miquelcms.rickmorty.feature.characters.ui.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.component.RmStatusLabel
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.designsystem.theme.Spacing
import com.miquelcms.rickmorty.feature.characters.ui.R
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterDetailUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi

@Composable
internal fun CharacterInfo(
    character: CharacterDetailUi,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(
                text = character.name.uppercase(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.semantics { heading() },
            )
            RmStatusLabel(
                text = stringResource(character.status.labelRes),
                type = character.status.type,
            )
        }
        InfoRow(
            label = stringResource(R.string.character_detail_species),
            value = character.species,
        )
        if (character.type != null) {
            InfoRow(
                label = stringResource(R.string.character_detail_type),
                value = character.type,
            )
        }
        InfoRow(
            label = stringResource(R.string.character_detail_gender),
            value = stringResource(character.gender.labelRes),
        )
        InfoRow(
            label = stringResource(R.string.character_detail_origin),
            value = character.originName,
        )
        InfoRow(
            label = stringResource(R.string.character_detail_location),
            value = character.locationName,
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Column(
        modifier = Modifier.semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@PreviewLightDark
@Composable
private fun CharacterInfoPreview() {
    RickMortyTheme {
        Surface {
            CharacterInfo(
                character = CharacterDetailUi(
                    id = 1,
                    name = "Rick Sanchez",
                    imageUrl = "",
                    status = CharacterStatusUi.ALIVE,
                    species = "Human",
                    type = "Scientist",
                    gender = CharacterGenderUi.MALE,
                    originName = "Earth (C-137)",
                    locationName = "Citadel of Ricks",
                ),
            )
        }
    }
}
