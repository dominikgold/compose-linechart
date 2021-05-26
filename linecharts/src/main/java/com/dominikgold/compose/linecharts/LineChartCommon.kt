package com.dominikgold.compose.linecharts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import com.dominikgold.compose.linecharts.hoverpopup.HoverPopup
import com.dominikgold.compose.linecharts.hoverpopup.HoverPopupState
import com.dominikgold.compose.linecharts.hoverpopup.detectHoverPopup
import com.dominikgold.compose.linecharts.models.LineChartConfig
import com.dominikgold.compose.linecharts.utils.Index
import com.dominikgold.compose.linecharts.utils.Point
import kotlin.math.roundToInt

@Composable
internal fun LineChartInternal(
    points: List<Point>,
    modifier: Modifier,
    config: LineChartConfig,
    hoverPopup: @Composable ((Index) -> Unit)?,
) {
    BoxWithConstraints(modifier) {
        with(LocalDensity.current) {
            val chartOutlineWidth = 1.dp.toPx()
            val lineGraphWidth = 2.dp.toPx()
            val chartWidth = constraints.maxWidth
            val chartHeight = constraints.constrainHeight((chartWidth * 0.75).roundToInt())
            val pointOffsets = points.calculateOffsetsForDrawing(IntSize(chartWidth, chartHeight), chartOutlineWidth)
            val canvasModifier = if (hoverPopup != null) {
                var hoverPopupState by remember { mutableStateOf<HoverPopupState?>(null) }
                hoverPopupState?.position?.let { hoverPopupPosition ->
                    HoverPopup(
                        hoverPopup = { hoverPopup(pointOffsets.indexOf(hoverPopupPosition)) },
                        hoverPopupPosition = hoverPopupPosition,
                        chartWidth = chartWidth,
                        chartHeight = chartHeight
                    )
                }
                Modifier.detectHoverPopup(pointOffsets) { newState ->
                    hoverPopupState = newState
                }
            } else {
                Modifier
            }
            Canvas(modifier = canvasModifier.size(chartWidth.toFloat().toDp(), chartHeight.toFloat().toDp())) {
                drawChartBackground(config.chartScaffoldColor, chartOutlineWidth)
                drawLineGraph(pointOffsets, config.lineColor, lineGraphWidth, chartOutlineWidth)
            }
        }
    }
}

private fun List<Point>.calculateOffsetsForDrawing(containerSize: IntSize, chartOutlineWidth: Float): List<Offset> = this.map { point ->
    // The y axis value needs to be inverted as the given percentages are from bottom to top while the on screen
    //  representation is from top to bottom
    val invertedYAxisPercentage = 1.0 - point.y
    val widthWithoutOutline = containerSize.width - chartOutlineWidth
    val heightWithoutOutline = containerSize.height - chartOutlineWidth
    return@map Offset(
        x = (widthWithoutOutline * point.x + chartOutlineWidth).toFloat(),
        y = heightWithoutOutline * invertedYAxisPercentage.toFloat(),
    )
}

private fun DrawScope.drawLineGraph(
    pointOffsets: List<Offset>,
    lineColor: Color,
    lineGraphStrokeWidth: Float,
    chartOutlineWidth: Float,
) {
    val heightWithoutOutline = this.size.height - chartOutlineWidth
    if (pointOffsets.size == 1) {
        drawLine(
            lineColor,
            start = Offset(chartOutlineWidth, heightWithoutOutline / 2f),
            end = Offset(this.size.width, heightWithoutOutline / 2f),
            strokeWidth = lineGraphStrokeWidth
        )
        return
    }

    drawPoints(
        points = pointOffsets,
        pointMode = PointMode.Polygon,
        color = lineColor,
        strokeWidth = lineGraphStrokeWidth,
        cap = StrokeCap.Round
    )
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
