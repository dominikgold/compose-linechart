package com.dominikgold.compose.linecharts

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SimpleLineChartConfig(
    chartScaffoldColor: Color = MaterialTheme.colors.onBackground,
    lineColor: Color = MaterialTheme.colors.primary,
    animationSpec: AnimationSpec<Float>? = tween(
        durationMillis = 1500,
        easing = CubicBezierEasing(0.5f, 0.0f, 0.1f, 0.9f),
    ),
) = LineChartConfig(chartScaffoldColor, lineColor, animationSpec)

class LineChartConfig internal constructor(
    val chartScaffoldColor: Color,
    val lineColor: Color,
    val animationSpec: AnimationSpec<Float>?,
)
