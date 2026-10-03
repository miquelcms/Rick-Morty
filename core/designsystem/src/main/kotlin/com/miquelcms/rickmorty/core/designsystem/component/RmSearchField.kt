package com.miquelcms.rickmorty.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.miquelcms.rickmorty.core.designsystem.R
import com.miquelcms.rickmorty.core.designsystem.icon.RmIcons
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme

@Composable
fun RmSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = { Text(text = placeholder) },
        leadingIcon = {
            Icon(painter = painterResource(RmIcons.Search), contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                RmIconButton(
                    icon = RmIcons.Close,
                    contentDescription = stringResource(R.string.search_clear),
                    onClick = { onQueryChange("") },
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
        ),
    )
}

@PreviewLightDark
@Composable
private fun RmSearchFieldPreview() {
    RickMortyTheme {
        Surface {
            RmSearchField(query = "Rick", onQueryChange = {}, placeholder = "Search by name")
        }
    }
}
