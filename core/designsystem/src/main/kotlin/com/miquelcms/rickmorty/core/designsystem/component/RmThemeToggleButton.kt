package com.miquelcms.rickmorty.core.designsystem.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.R
import com.miquelcms.rickmorty.core.designsystem.icon.RmIcons
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme

@Composable
fun RmThemeToggleButton(
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isDarkTheme) {
        RmIconButton(
            icon = RmIcons.LightMode,
            contentDescription = stringResource(R.string.theme_toggle_to_light),
            onClick = onClick,
            modifier = modifier,
        )
    } else {
        RmIconButton(
            icon = RmIcons.DarkMode,
            contentDescription = stringResource(R.string.theme_toggle_to_dark),
            onClick = onClick,
            modifier = modifier,
        )
    }
}

@PreviewLightDark
@Composable
private fun RmThemeToggleButtonPreview() {
    RickMortyTheme {
        Surface {
            RmThemeToggleButton(isDarkTheme = isSystemInDarkTheme(), onClick = {})
        }
    }
}
