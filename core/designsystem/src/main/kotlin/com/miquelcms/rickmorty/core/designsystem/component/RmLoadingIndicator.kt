package com.miquelcms.rickmorty.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.designsystem.theme.Spacing
import com.miquelcms.rickmorty.core.designsystem.theme.Stroke

private val IndicatorSize = 24.dp

@Composable
fun RmLoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(IndicatorSize),
        color = MaterialTheme.colorScheme.onSurface,
        strokeWidth = Stroke.medium,
    )
}

@PreviewLightDark
@Composable
private fun RmLoadingIndicatorPreview() {
    RickMortyTheme {
        Surface {
            RmLoadingIndicator(modifier = Modifier.padding(Spacing.md))
        }
    }
}
