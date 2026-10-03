package com.miquelcms.rickmorty.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class DynamicString(val value: String) : UiText

    data class StringResource(
        @StringRes val id: Int,
        val args: List<Any> = emptyList(),
    ) : UiText
}

@Composable
fun UiText.asString(): String =
    when (this) {
        is UiText.DynamicString -> value
        is UiText.StringResource -> stringResource(id, *args.toTypedArray())
    }

fun UiText.asString(context: Context): String =
    when (this) {
        is UiText.DynamicString -> value
        is UiText.StringResource -> context.getString(id, *args.toTypedArray())
    }
