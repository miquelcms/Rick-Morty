package com.miquelcms.rickmorty.feature.characters.ui.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.component.RmBottomSheet
import com.miquelcms.rickmorty.core.designsystem.component.RmButton
import com.miquelcms.rickmorty.core.designsystem.component.RmFilterChip
import com.miquelcms.rickmorty.core.designsystem.component.RmOutlinedButton
import com.miquelcms.rickmorty.core.designsystem.component.RmTextField
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.designsystem.theme.Spacing
import com.miquelcms.rickmorty.feature.characters.ui.R
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterFiltersUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi

internal val FiltersSaver = listSaver<CharacterFiltersUi, String>(
    save = { listOf(it.status?.name.orEmpty(), it.gender?.name.orEmpty(), it.species, it.type) },
    restore = {
        CharacterFiltersUi(
            status = it[0].ifEmpty { null }?.let(CharacterStatusUi::valueOf),
            gender = it[1].ifEmpty { null }?.let(CharacterGenderUi::valueOf),
            species = it[2],
            type = it[3],
        )
    },
)

@Composable
internal fun CharacterFiltersSheet(
    filters: CharacterFiltersUi,
    onApply: (CharacterFiltersUi) -> Unit,
    onDismiss: () -> Unit,
) {
    var draft by rememberSaveable(stateSaver = FiltersSaver) { mutableStateOf(filters) }

    RmBottomSheet(onDismiss = onDismiss) { hide ->
        CharacterFiltersContent(
            filters = draft,
            onFiltersChange = { draft = it },
            onApplyClick = {
                onApply(draft)
                hide()
            },
            onClearClick = {
                onApply(CharacterFiltersUi())
                hide()
            },
        )
    }
}

@Composable
internal fun CharacterFiltersContent(
    filters: CharacterFiltersUi,
    onFiltersChange: (CharacterFiltersUi) -> Unit,
    onApplyClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.md)
            .padding(bottom = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Text(
            text = stringResource(R.string.character_filters_title).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.semantics { heading() },
        )

        SectionTitle(text = stringResource(R.string.character_filters_status))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            CharacterStatusUi.entries.forEach { status ->
                RmFilterChip(
                    text = stringResource(status.labelRes),
                    selected = filters.status == status,
                    onClick = {
                        val newStatus = if (filters.status == status) null else status
                        onFiltersChange(filters.copy(status = newStatus))
                    },
                )
            }
        }

        SectionTitle(text = stringResource(R.string.character_filters_gender))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            CharacterGenderUi.entries.forEach { gender ->
                RmFilterChip(
                    text = stringResource(gender.labelRes),
                    selected = filters.gender == gender,
                    onClick = {
                        val newGender = if (filters.gender == gender) null else gender
                        onFiltersChange(filters.copy(gender = newGender))
                    },
                )
            }
        }

        RmTextField(
            value = filters.species,
            onValueChange = { onFiltersChange(filters.copy(species = it)) },
            label = stringResource(R.string.character_filters_species),
            imeAction = ImeAction.Next,
        )
        RmTextField(
            value = filters.type,
            onValueChange = { onFiltersChange(filters.copy(type = it)) },
            label = stringResource(R.string.character_filters_type),
        )

        RmButton(
            text = stringResource(R.string.character_filters_apply),
            onClick = onApplyClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.md),
        )
        RmOutlinedButton(
            text = stringResource(R.string.character_filters_clear),
            onClick = onClearClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .padding(top = Spacing.sm)
            .semantics { heading() },
    )
}

@PreviewLightDark
@Composable
private fun CharacterFiltersContentPreview() {
    RickMortyTheme {
        Surface {
            CharacterFiltersContent(
                filters = CharacterFiltersUi(
                    status = CharacterStatusUi.ALIVE,
                    species = "Human",
                ),
                onFiltersChange = {},
                onApplyClick = {},
                onClearClick = {},
            )
        }
    }
}
