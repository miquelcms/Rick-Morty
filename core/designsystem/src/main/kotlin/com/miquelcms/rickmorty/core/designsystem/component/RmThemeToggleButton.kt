package com.miquelcms.rickmorty.core.designsystem.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.R
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme

@Composable
fun RmThemeToggleButton(
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        if (isDarkTheme) {
            Icon(
                painter = painterResource(R.drawable.ic_light_mode),
                contentDescription = stringResource(R.string.theme_toggle_to_light),
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_dark_mode),
                contentDescription = stringResource(R.string.theme_toggle_to_dark),
            )
        }
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
