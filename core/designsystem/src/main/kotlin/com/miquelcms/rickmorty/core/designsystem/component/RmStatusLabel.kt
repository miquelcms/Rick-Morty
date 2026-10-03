package com.miquelcms.rickmorty.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.designsystem.theme.Spacing

enum class RmStatusType {
    POSITIVE,
    NEGATIVE,
    NEUTRAL,
}

@Composable
fun RmStatusLabel(
    text: String,
    type: RmStatusType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = type.color(), shape = CircleShape),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun RmStatusType.color(): Color =
    when (this) {
        RmStatusType.POSITIVE -> MaterialTheme.colorScheme.tertiary
        RmStatusType.NEGATIVE -> MaterialTheme.colorScheme.error
        RmStatusType.NEUTRAL -> MaterialTheme.colorScheme.outline
    }

@PreviewLightDark
@Composable
private fun RmStatusLabelPreview() {
    RickMortyTheme {
        Surface {
            Column(
                modifier = Modifier.padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                RmStatusLabel(text = "Alive", type = RmStatusType.POSITIVE)
                RmStatusLabel(text = "Dead", type = RmStatusType.NEGATIVE)
                RmStatusLabel(text = "Unknown", type = RmStatusType.NEUTRAL)
            }
        }
    }
}
