package com.dominikgold.compose.linecharts

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SimpleLineChart(
    lineChartState: SimpleLineChartState,
    modifier: Modifier = Modifier,
    chartScaffoldColor: Color = MaterialTheme.colors.onBackground,
    lineColor: Color = MaterialTheme.colors.primary,
    animationSpec: AnimationSpec<Float>? = tween(
        durationMillis = 1500,
        easing = CubicBezierEasing(0.5f, 0.0f, 0.1f, 0.9f),
    ),
) {
    SimpleLineChartInternal(
        data = lineChartState.currentDataPoints,
        previousData = lineChartState.previousDataPoints,
        modifier = modifier,
        chartScaffoldColor = chartScaffoldColor,
        lineColor = lineColor,
        animationSpec = animationSpec,
    )
}

@Composable
private fun SimpleLineChartInternal(
    data: NormalizedSimpleLineChartData,
    previousData: NormalizedSimpleLineChartData,
    modifier: Modifier,
    chartScaffoldColor: Color,
    lineColor: Color,
    animationSpec: AnimationSpec<Float>?,
) {
    if (animationSpec != null) {
        SimpleAnimatedLineChart(
            data = data,
            previousData = previousData,
            animationSpec = animationSpec,
            modifier = modifier,
            chartScaffoldColor = chartScaffoldColor,
            lineColor = lineColor,
        )
    } else {
        LineChartInternal(
            points = createPoints(data.normalizedYAxisValues),
            modifier = modifier,
            chartScaffoldColor = chartScaffoldColor,
            lineColor = lineColor,
        )
    }
}

@Composable
private fun SimpleAnimatedLineChart(
    data: NormalizedSimpleLineChartData,
    previousData: NormalizedSimpleLineChartData,
    animationSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier,
    chartScaffoldColor: Color = MaterialTheme.colors.onBackground,
    lineColor: Color = MaterialTheme.colors.primary,
) {
    if (data.normalizedYAxisValues.isEmpty()) {
        LineChartInternal(
            points = listOf(),
            modifier = modifier,
            chartScaffoldColor = chartScaffoldColor,
            lineColor = lineColor,
        )
        return
    }

    var animationProgress by remember { mutableStateOf(0f) }
    LaunchedEffect(data) {
        animate(initialValue = 0f, targetValue = 1f, animationSpec = animationSpec) { value, _ ->
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
        chartScaffoldColor = chartScaffoldColor,
        lineColor = lineColor,
    )
}

@Composable
private fun LineChartInternal(
    points: List<Point>,
    modifier: Modifier,
    chartScaffoldColor: Color,
    lineColor: Color,
) {
    BoxWithConstraints(modifier) {
        val chartWidth = constraints.maxWidth
        val chartHeight = constraints.constrainHeight((chartWidth * 0.75).roundToInt())
        with(LocalDensity.current) {
            val chartOutlineWidth = 1.dp.toPx()
            val lineGraphWidth = 2.dp.toPx()
            Canvas(modifier = Modifier.size(chartWidth.toFloat().toDp(), chartHeight.toFloat().toDp())) {
                drawChartBackground(chartScaffoldColor, chartOutlineWidth)
                drawLineGraph(points, lineColor, lineGraphWidth, chartOutlineWidth)
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
    drawPoints(points = points.mapIndexed { index, point ->
        // The y axis value needs to be inverted as the given percentages are from bottom to top while the on screen
        //  representation is from top to bottom
        val invertedYAxisPercentage = 1.0 - point.y
        return@mapIndexed Offset(
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

@Preview(showBackground = true)
@Composable
fun SimpleLineChartPreview() {
    Box(Modifier
            .background(MaterialTheme.colors.background)
            .padding(16.dp)) {
        val lineChartState = remember { SimpleLineChartState() }
        lineChartState.updateDataPoints(listOf(
            SimpleLineChartDataPoint(yAxisValue = 90.0, "6 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 89.0, "5 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 91.0, "4 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 93.0, "3 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 90.0, "2 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 92.0, "1 w ago"),
        ))
        SimpleLineChart(
            lineChartState = lineChartState,
            animationSpec = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedLineChartPreview() {
    val lineChartState = remember { SimpleLineChartState() }
    lineChartState.updateDataPoints(listOf(
        SimpleLineChartDataPoint(yAxisValue = 90.0, "6 w ago"),
        SimpleLineChartDataPoint(yAxisValue = 89.0, "5 w ago"),
        SimpleLineChartDataPoint(yAxisValue = 91.0, "4 w ago"),
        SimpleLineChartDataPoint(yAxisValue = 93.0, "3 w ago"),
        SimpleLineChartDataPoint(yAxisValue = 90.0, "2 w ago"),
        SimpleLineChartDataPoint(yAxisValue = 92.0, "1 w ago"),
    ))
    Column(Modifier
               .background(MaterialTheme.colors.background)
               .padding(16.dp)) {
        SimpleLineChart(lineChartState, animationSpec = tween(500))
        Button(onClick = {
            lineChartState.updateDataPoints(listOf(
                SimpleLineChartDataPoint(yAxisValue = 88.0, "6 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 90.0, "5 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 92.0, "4 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 89.0, "3 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 87.0, "2 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 91.0, "1 w ago"),
            ))
        }) {
            Text("Click me!")
        }
    }
}
