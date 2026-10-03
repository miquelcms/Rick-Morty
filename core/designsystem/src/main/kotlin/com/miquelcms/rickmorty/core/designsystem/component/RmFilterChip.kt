package com.miquelcms.rickmorty.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.designsystem.theme.Spacing
import com.miquelcms.rickmorty.core.designsystem.theme.Stroke

@Composable
fun RmFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text = text, style = MaterialTheme.typography.labelLarge) },
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurface,
            selectedContainerColor = MaterialTheme.colorScheme.onSurface,
            selectedLabelColor = MaterialTheme.colorScheme.surface,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderColor = MaterialTheme.colorScheme.onSurface,
            borderWidth = Stroke.thin,
            selectedBorderWidth = Stroke.thin,
        ),
    )
}

@PreviewLightDark
@Composable
private fun RmFilterChipPreview() {
    RickMortyTheme {
        Surface {
            Row(
                modifier = Modifier.padding(Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                RmFilterChip(text = "Alive", selected = true, onClick = {})
                RmFilterChip(text = "Dead", selected = false, onClick = {})
            }
        }
    }
}
