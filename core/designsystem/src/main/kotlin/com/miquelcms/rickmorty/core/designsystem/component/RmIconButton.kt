package com.miquelcms.rickmorty.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.icon.RmIcons
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme

@Composable
fun RmIconButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        BadgedBox(
            badge = {
                if (badgeCount > 0) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                    ) {
                        Text(text = badgeCount.toString())
                    }
                }
            },
        ) {
            Icon(painter = painterResource(icon), contentDescription = contentDescription)
        }
    }
}

@PreviewLightDark
@Composable
private fun RmIconButtonPreview() {
    RickMortyTheme {
        Surface {
            RmIconButton(
                icon = RmIcons.Filter,
                contentDescription = "Filters",
                onClick = {},
                badgeCount = 2,
            )
        }
    }
}
