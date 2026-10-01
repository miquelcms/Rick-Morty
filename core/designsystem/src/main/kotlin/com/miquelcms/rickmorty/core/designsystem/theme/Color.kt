package com.miquelcms.rickmorty.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val Neutral0 = Color(0xFFFFFFFF)
private val Neutral50 = Color(0xFFFAFAFA)
private val Neutral100 = Color(0xFFF5F5F5)
private val Neutral200 = Color(0xFFEDEDED)
private val Neutral300 = Color(0xFFD6D6D6)
private val Neutral400 = Color(0xFF9E9E9E)
private val Neutral600 = Color(0xFF6B6B6B)
private val Neutral700 = Color(0xFF2B2B2B)
private val Neutral800 = Color(0xFF1F1F1F)
private val Neutral850 = Color(0xFF171717)
private val Neutral900 = Color(0xFF111111)
private val Neutral950 = Color(0xFF0A0A0A)

private val PortalGreen = Color(0xFF97CE4C)
private val DeepGreen = Color(0xFF3B7A00)

internal val LightColorScheme = lightColorScheme(
    primary = Neutral950,
    onPrimary = Neutral0,
    primaryContainer = Neutral200,
    onPrimaryContainer = Neutral950,
    inversePrimary = Neutral0,
    secondary = Neutral600,
    onSecondary = Neutral0,
    secondaryContainer = Neutral950,
    onSecondaryContainer = Neutral0,
    tertiary = DeepGreen,
    onTertiary = Neutral0,
    tertiaryContainer = PortalGreen,
    onTertiaryContainer = Neutral950,
    background = Neutral0,
    onBackground = Neutral950,
    surface = Neutral0,
    onSurface = Neutral950,
    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral600,
    inverseSurface = Neutral900,
    inverseOnSurface = Neutral50,
    outline = Neutral400,
    outlineVariant = Neutral300,
    surfaceBright = Neutral0,
    surfaceDim = Neutral300,
    surfaceContainerLowest = Neutral0,
    surfaceContainerLow = Neutral50,
    surfaceContainer = Neutral100,
    surfaceContainerHigh = Neutral100,
    surfaceContainerHighest = Neutral200,
)

internal val DarkColorScheme = darkColorScheme(
    primary = Neutral50,
    onPrimary = Neutral950,
    primaryContainer = Neutral700,
    onPrimaryContainer = Neutral50,
    inversePrimary = Neutral950,
    secondary = Neutral400,
    onSecondary = Neutral950,
    secondaryContainer = Neutral50,
    onSecondaryContainer = Neutral950,
    tertiary = PortalGreen,
    onTertiary = Neutral950,
    tertiaryContainer = PortalGreen,
    onTertiaryContainer = Neutral950,
    background = Neutral950,
    onBackground = Neutral50,
    surface = Neutral950,
    onSurface = Neutral50,
    surfaceVariant = Neutral850,
    onSurfaceVariant = Neutral400,
    inverseSurface = Neutral50,
    inverseOnSurface = Neutral900,
    outline = Neutral600,
    outlineVariant = Neutral700,
    surfaceBright = Neutral700,
    surfaceDim = Neutral950,
    surfaceContainerLowest = Neutral950,
    surfaceContainerLow = Neutral900,
    surfaceContainer = Neutral850,
    surfaceContainerHigh = Neutral800,
    surfaceContainerHighest = Neutral700,
)
