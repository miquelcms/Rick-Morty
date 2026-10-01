package com.miquelcms.rickmorty.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.miquelcms.rickmorty.core.designsystem.R

private fun jostFont(weight: FontWeight) = Font(
    resId = R.font.jost,
    weight = weight,
    variationSettings = FontVariation.Settings(weight, FontStyle.Normal),
)

private val Jost = FontFamily(
    jostFont(FontWeight.Light),
    jostFont(FontWeight.Normal),
    jostFont(FontWeight.Medium),
)

private fun TextStyle.jost(
    weight: FontWeight,
    letterSpacing: TextUnit = this.letterSpacing,
) = copy(fontFamily = Jost, fontWeight = weight, letterSpacing = letterSpacing)

private val Baseline = Typography()

internal val RmTypography = Typography(
    displayLarge = Baseline.displayLarge.jost(FontWeight.Light),
    displayMedium = Baseline.displayMedium.jost(FontWeight.Light),
    displaySmall = Baseline.displaySmall.jost(FontWeight.Light),
    headlineLarge = Baseline.headlineLarge.jost(FontWeight.Light),
    headlineMedium = Baseline.headlineMedium.jost(FontWeight.Light),
    headlineSmall = Baseline.headlineSmall.jost(FontWeight.Light),
    titleLarge = Baseline.titleLarge.jost(FontWeight.Normal, letterSpacing = 1.sp),
    titleMedium = Baseline.titleMedium.jost(FontWeight.Medium, letterSpacing = 1.sp),
    titleSmall = Baseline.titleSmall.jost(FontWeight.Medium, letterSpacing = 1.sp),
    bodyLarge = Baseline.bodyLarge.jost(FontWeight.Normal),
    bodyMedium = Baseline.bodyMedium.jost(FontWeight.Normal),
    bodySmall = Baseline.bodySmall.jost(FontWeight.Normal),
    labelLarge = Baseline.labelLarge.jost(FontWeight.Medium, letterSpacing = 1.5.sp),
    labelMedium = Baseline.labelMedium.jost(FontWeight.Medium, letterSpacing = 1.5.sp),
    labelSmall = Baseline.labelSmall.jost(FontWeight.Medium, letterSpacing = 1.5.sp),
)
