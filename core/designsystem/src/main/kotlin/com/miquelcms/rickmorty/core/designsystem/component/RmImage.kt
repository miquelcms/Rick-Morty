package com.miquelcms.rickmorty.core.designsystem.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@Composable
fun RmImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    sharedElementKey: Any? = null,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    val canBeShared = sharedElementKey != null &&
            sharedTransitionScope != null &&
            animatedVisibilityScope != null
    val sharedElementModifier = if (canBeShared) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = sharedElementKey),
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    } else {
        Modifier
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier
            .then(sharedElementModifier)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentScale = contentScale,
    )
}
