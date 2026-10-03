package com.miquelcms.rickmorty.core.designsystem.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.R
import com.miquelcms.rickmorty.core.designsystem.icon.RmIcons
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme

@Composable
fun RmBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RmIconButton(
        icon = RmIcons.ArrowBack,
        contentDescription = stringResource(R.string.navigate_back),
        onClick = onClick,
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun RmBackButtonPreview() {
    RickMortyTheme {
        Surface {
            RmBackButton(onClick = {})
        }
    }
}
