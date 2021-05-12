package com.dominikgold.compose.linecharts

import androidx.compose.animation.core.animate
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SimpleLineChart(
    lineChartState: SimpleLineChartState,
    modifier: Modifier = Modifier,
    config: LineChartConfig = SimpleLineChartConfig(),
    hoverPopup: @Composable ((SimpleLineChartDataPoint) -> Unit)? = null,
) {
    SimpleLineChartInternal(
        lineChartState = lineChartState,
        modifier = modifier,
        config = config,
        hoverPopup = hoverPopup,
    )
}

@Composable
private fun SimpleLineChartInternal(
    lineChartState: SimpleLineChartState,
    modifier: Modifier,
    config: LineChartConfig,
    hoverPopup: @Composable ((SimpleLineChartDataPoint) -> Unit)?,
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
    hoverPopup: @Composable ((SimpleLineChartDataPoint) -> Unit)?,
) {
    require(config.animationSpec != null) { "AnimationSpec must not be null. This is a library internal error. " }
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
    val points = interpolateBetweenYAxisData(
        originalYAxisData = previousData.normalizedYAxisValues,
        targetYAxisData = data.normalizedYAxisValues,
        progress = animationProgress,
    )
    LineChartInternal(
        points = points,
        modifier = modifier,
        config = config,
        hoverPopup = hoverPopup,
    )
}

@Composable
private fun LineChartInternal(
    points: List<Point>,
    modifier: Modifier,
    config: LineChartConfig,
    hoverPopup: @Composable ((SimpleLineChartDataPoint) -> Unit)?,
) {
    BoxWithConstraints(modifier) {
        val chartWidth = constraints.maxWidth
        val chartHeight = constraints.constrainHeight((chartWidth * 0.75).roundToInt())
        with(LocalDensity.current) {
            val chartOutlineWidth = 1.dp.toPx()
            val lineGraphWidth = 2.dp.toPx()
            Canvas(modifier = Modifier.size(chartWidth.toFloat().toDp(), chartHeight.toFloat().toDp())) {
                drawChartBackground(config.chartScaffoldColor, chartOutlineWidth)
                drawLineGraph(points, config.lineColor, lineGraphWidth, chartOutlineWidth)
            }
        }
    }
}

private fun DrawScope.drawLineGraph(
    points: List<Point>,
    lineColor: Color,
    lineGraphWidth: Float,
    chartOutlineWidth: Float,
) {
    val heightWithoutOutline = this.size.height - chartOutlineWidth
    if (points.size == 1) {
        drawLine(lineColor,
                 start = Offset(chartOutlineWidth, heightWithoutOutline / 2f),
                 end = Offset(this.size.width, heightWithoutOutline / 2f),
                 strokeWidth = lineGraphWidth)
        return
    }

    val widthWithoutOutline = this.size.width - chartOutlineWidth
    drawPoints(points = points.map { point ->
        // The y axis value needs to be inverted as the given percentages are from bottom to top while the on screen
        //  representation is from top to bottom
        val invertedYAxisPercentage = 1.0 - point.y
        return@map Offset(
            x = (chartOutlineWidth + widthWithoutOutline * point.x).toFloat(),
            y = heightWithoutOutline * invertedYAxisPercentage.toFloat(),
        )
    }, pointMode = PointMode.Polygon, color = lineColor, strokeWidth = lineGraphWidth, cap = StrokeCap.Round)
}

private fun DrawScope.drawChartBackground(color: Color, outlineWidth: Float) {
    drawLine(
        color,
        start = Offset.Zero,
        end = Offset(0f, this.size.height - 16f - outlineWidth),
        strokeWidth = outlineWidth,
    )
    drawArc(
        color,
        startAngle = 180f,
        sweepAngle = -90f,
        useCenter = false,
        topLeft = Offset(0f, this.size.height - 32f - outlineWidth),
        size = Size(32f, 32f),
        style = Stroke(width = outlineWidth),
    )
    drawLine(
        color,
        start = Offset(16f, this.size.height - outlineWidth),
        end = Offset(this.size.width, this.size.height - outlineWidth),
        strokeWidth = outlineWidth,
    )
}
