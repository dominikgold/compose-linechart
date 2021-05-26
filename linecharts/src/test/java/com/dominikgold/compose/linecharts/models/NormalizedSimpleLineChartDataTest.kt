package com.dominikgold.compose.linecharts.models

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInRange
import org.junit.Test

class NormalizedSimpleLineChartDataTest {

    @Test
    fun `original data points are normalized to percentages with a 10 percent min and max padding`() {
        val originalDataPoints = listOf(
            SimpleLineChartDataPoint(yAxisValue = 90.0, null),
            SimpleLineChartDataPoint(yAxisValue = 89.0, null),
            SimpleLineChartDataPoint(yAxisValue = 90.0, null),
            SimpleLineChartDataPoint(yAxisValue = 92.0, null),
            SimpleLineChartDataPoint(yAxisValue = 94.0, null),
            SimpleLineChartDataPoint(yAxisValue = 91.5, null),
        )

        val normalizedDataPoints = NormalizedSimpleLineChartData(originalDataPoints, padding = 0.1)

        // With the 10% min and max 'padding' and a total range of 5.0 (94.0 - 89.0), a percentage of zero would be
        // equal to 88.5 and a percentage of 100 equal to 94.5
        // The expected values here are based upon this assumption.
        normalizedDataPoints.normalizedYAxisValues[0] shouldBeEqualTo 0.25 // 90.0
        normalizedDataPoints.normalizedYAxisValues[1] shouldBeInRange 0.0833..0.0834 // 89.0 - be lenient for rounding
        normalizedDataPoints.normalizedYAxisValues[2] shouldBeEqualTo 0.25 // 90.0
        normalizedDataPoints.normalizedYAxisValues[3] shouldBeInRange 0.5833..0.5834 // 92.0 - be lenient for rounding
        normalizedDataPoints.normalizedYAxisValues[4] shouldBeInRange 0.9166..0.9167 // 94.0 - be lenient for rounding
        normalizedDataPoints.normalizedYAxisValues[5] shouldBeEqualTo 0.5 // 91.5
    }

    @Test
    fun `gracefully handles empty original data points`() {
        val originalDataPoints = listOf<SimpleLineChartDataPoint>()

        val normalizedDataPoints = NormalizedSimpleLineChartData(originalDataPoints, padding = 0.1)

        normalizedDataPoints.normalizedYAxisValues.shouldBeEmpty()
    }

    @Test
    fun `x axis descriptions are taken from original data points`() {
        val originalDataPoints = listOf(
            SimpleLineChartDataPoint(yAxisValue = 90.0, "data point 1"),
            SimpleLineChartDataPoint(yAxisValue = 89.0, "data point 2"),
            SimpleLineChartDataPoint(yAxisValue = 91.5, "data point 3"),
        )

        val normalizedDataPoints = NormalizedSimpleLineChartData(originalDataPoints, 0.1)

        normalizedDataPoints.xAxisDescriptions[0] shouldBeEqualTo "data point 1"
        normalizedDataPoints.xAxisDescriptions[1] shouldBeEqualTo "data point 2"
        normalizedDataPoints.xAxisDescriptions[2] shouldBeEqualTo "data point 3"
    }

}