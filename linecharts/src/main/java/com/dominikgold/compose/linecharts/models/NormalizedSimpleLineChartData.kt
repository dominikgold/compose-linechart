package com.dominikgold.compose.linecharts.models

import com.dominikgold.compose.linecharts.utils.Percentage

internal data class NormalizedSimpleLineChartData(
    internal val original: List<SimpleLineChartDataPoint>,
    private val customDataRange: ClosedRange<Double>?,
) {

    init {
        require(customDataRange == null || original.all { it.yAxisValue in customDataRange }) {
            "When specifying a custom data range, all given data points must be within that range"
        }
    }

    val dataRange: ClosedRange<Double> = customDataRange ?: original.let { originalDataPoints ->
        if (originalDataPoints.isEmpty()) {
            return@let 0.0..1.0
        }

        val defaultPadding = 0.1
        val maxYAxisValue = originalDataPoints.maxOf { it.yAxisValue }
        val minYAxisValue = originalDataPoints.minOf { it.yAxisValue }
        val yAxisRange = maxYAxisValue - minYAxisValue
        val maxYAxisValueWithPadding = maxYAxisValue + yAxisRange * defaultPadding
        val minYAxisValueWithPadding = minYAxisValue - yAxisRange * defaultPadding
        return@let minYAxisValueWithPadding..maxYAxisValueWithPadding
    }

    val normalizedYAxisValues: List<Percentage> = original.let { originalDataPoints ->
        if (originalDataPoints.isEmpty()) {
            return@let listOf()
        }

        val upperDataRangeEnd = dataRange.endInclusive
        val lowerDataRangeEnd = dataRange.start
        val dataRangeSize = upperDataRangeEnd - lowerDataRangeEnd
        return@let if (dataRangeSize == 0.0) {
            // return two y axis values representing a horizontal line in the middle of a line chart
            listOf(0.5, 0.5)
        } else {
            originalDataPoints.map {
                (it.yAxisValue - lowerDataRangeEnd) / dataRangeSize
            }
        }
    }

}