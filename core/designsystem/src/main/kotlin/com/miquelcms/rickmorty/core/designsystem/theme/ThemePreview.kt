package com.miquelcms.rickmorty.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark

@PreviewLightDark
@Composable
private fun ThemePreview() {
    RickMortyTheme {
        Surface {
            Column(
                modifier = Modifier.padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                Text("Rick Sanchez", style = MaterialTheme.typography.headlineMedium)
                Text("CHARACTERS", style = MaterialTheme.typography.titleMedium)
                Text("Earth (C-137)", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "HUMAN · ALIVE",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    ColorBox(MaterialTheme.colorScheme.primary)
                    ColorBox(MaterialTheme.colorScheme.secondary)
                    ColorBox(MaterialTheme.colorScheme.tertiary)
                    ColorBox(MaterialTheme.colorScheme.surfaceVariant)
                    ColorBox(MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
private fun ColorBox(color: Color) {
    Box(Modifier.size(Spacing.xl).background(color))
}
