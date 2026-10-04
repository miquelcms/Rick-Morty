package com.miquelcms.rickmorty.feature.characters.ui.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.miquelcms.rickmorty.core.designsystem.component.RmImage
import com.miquelcms.rickmorty.core.designsystem.component.RmStatusLabel
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.designsystem.theme.Spacing
import com.miquelcms.rickmorty.feature.characters.ui.R
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterImageKey
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterUi

@Composable
internal fun CharacterCard(
    character: CharacterUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable(
            onClickLabel = stringResource(R.string.character_card_open),
            role = Role.Button,
            onClick = onClick,
        ),
    ) {
        RmImage(
            url = character.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            sharedElementKey = CharacterImageKey(character.id),
        )
        Column(
            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = character.name.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            RmStatusLabel(
                text = stringResource(character.status.labelRes),
                type = character.status.type,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun CharacterCardPreview() {
    RickMortyTheme {
        Surface {
            CharacterCard(
                character = CharacterUi(
                    id = 1,
                    name = "Rick Sanchez",
                    imageUrl = "",
                    status = CharacterStatusUi.ALIVE,
                ),
                onClick = {},
                modifier = Modifier.width(180.dp),
            )
        }
    }
}
