package com.dominikgold.compose.linecharts

import androidx.compose.animation.core.animate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dominikgold.compose.linecharts.models.LineChartConfig
import com.dominikgold.compose.linecharts.models.NormalizedSimpleLineChartData
import com.dominikgold.compose.linecharts.models.SimpleLineChartState
import com.dominikgold.compose.linecharts.utils.Index
import com.dominikgold.compose.linecharts.utils.createPoints
import com.dominikgold.compose.linecharts.utils.interpolateBetweenYAxisData

@Composable
internal fun SimpleLineChartInternal(
    lineChartState: SimpleLineChartState,
    modifier: Modifier,
    config: LineChartConfig,
    hoverPopup: @Composable ((Index) -> Unit)?,
) {
    if (config.animationSpec != null) {
        SimpleAnimatedLineChart(
            data = lineChartState.currentDataPoints,
            previousData = lineChartState.previousDataPoints,
            config = config,
            modifier = modifier,
            hoverPopup = hoverPopup,
        )
    } else {
        LineChartInternal(
            points = createPoints(lineChartState.currentDataPoints.normalizedYAxisValues),
            modifier = modifier,
            config = config,
            hoverPopup = hoverPopup,
        )
    }
}

@Composable
private fun SimpleAnimatedLineChart(
    data: NormalizedSimpleLineChartData,
    previousData: NormalizedSimpleLineChartData,
    modifier: Modifier = Modifier,
    config: LineChartConfig,
    hoverPopup: @Composable ((Index) -> Unit)?,
) {
    require(config.animationSpec != null) { "AnimationSpec must not be null. This is a library internal error." }
    if (data.normalizedYAxisValues.isEmpty()) {
        LineChartInternal(
            points = listOf(),
            modifier = modifier,
            config = config,
            hoverPopup = hoverPopup,
        )
        return
    }

    var animationProgress by remember { mutableStateOf(0f) }
    LaunchedEffect(data) {
        animate(initialValue = 0f, targetValue = 1f, animationSpec = config.animationSpec) { value, _ ->
            animationProgress = value
        }
    }
    val points = if (animationProgress < 1f) {
        interpolateBetweenYAxisData(
            originalYAxisData = previousData.normalizedYAxisValues,
            targetYAxisData = data.normalizedYAxisValues,
            progress = animationProgress,
        )
    } else {
        createPoints(data.normalizedYAxisValues)
    }
    LineChartInternal(
        points = points,
        modifier = modifier,
        config = config,
        // Disable hover popups until after the animation is completed
        hoverPopup = if (animationProgress < 1f) null else hoverPopup,
    )
}