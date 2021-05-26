package com.dominikgold.compose.linecharts.models

import com.dominikgold.compose.linecharts.utils.Percentage

internal data class NormalizedSimpleLineChartData(
    internal val original: List<SimpleLineChartDataPoint>,
    private val padding: Double,
) {

    val normalizedYAxisValues: List<Percentage> = original.let { originalDataPoints ->
        if (originalDataPoints.isEmpty()) {
            return@let listOf()
        }

        val maxYAxisValue = originalDataPoints.maxOf { it.yAxisValue }
        val minYAxisValue = originalDataPoints.minOf { it.yAxisValue }
        val yAxisRange = maxYAxisValue - minYAxisValue
        val maxYAxisValueWithPadding = maxYAxisValue + yAxisRange * padding
        val minYAxisValueWithPadding = minYAxisValue - yAxisRange * padding
        val yAxisRangeWithPadding = maxYAxisValueWithPadding - minYAxisValueWithPadding
        return@let if (yAxisRangeWithPadding == 0.0) {
            // return two y axis values representing a horizontal line in the middle of a line chart
            listOf(0.5, 0.5)
        } else {
            originalDataPoints.map {
                (it.yAxisValue - minYAxisValueWithPadding) / yAxisRangeWithPadding
            }
        }
    }

    val xAxisDescriptions: List<String?> = original.map { it.description }

}