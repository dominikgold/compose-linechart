package com.dominikgold.compose.linecharts.models

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInRange
import org.junit.Test

class NormalizedSimpleLineChartDataTest {

    @Test
    fun `original data points are normalized to percentages with a 10 percent min and max padding when no custom data range is given`() {
        val originalDataPoints = listOf(
            SimpleLineChartDataPoint(yAxisValue = 90.0, null),
            SimpleLineChartDataPoint(yAxisValue = 89.0, null),
            SimpleLineChartDataPoint(yAxisValue = 90.0, null),
            SimpleLineChartDataPoint(yAxisValue = 92.0, null),
            SimpleLineChartDataPoint(yAxisValue = 94.0, null),
            SimpleLineChartDataPoint(yAxisValue = 91.5, null),
        )

        val normalizedDataPoints = NormalizedSimpleLineChartData(originalDataPoints, customDataRange = null)

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
    fun `data range is calculated with a 10 percent padding when no custom data range is given`() {
        val originalDataPoints = listOf(
            SimpleLineChartDataPoint(yAxisValue = 90.0, null),
            SimpleLineChartDataPoint(yAxisValue = 89.0, null),
            SimpleLineChartDataPoint(yAxisValue = 90.0, null),
            SimpleLineChartDataPoint(yAxisValue = 92.0, null),
            SimpleLineChartDataPoint(yAxisValue = 94.0, null),
            SimpleLineChartDataPoint(yAxisValue = 91.5, null),
        )

        val normalizedDataPoints = NormalizedSimpleLineChartData(originalDataPoints, customDataRange = null)

        // With the 10% min and max 'padding' and a total range of 5.0 (94.0 - 89.0), a percentage of zero would be
        // equal to 88.5 and a percentage of 100 equal to 94.5
        normalizedDataPoints.dataRange shouldBeEqualTo 88.5..94.5
    }

    @Test
    fun `gracefully handles empty original data points`() {
        val originalDataPoints = listOf<SimpleLineChartDataPoint>()

        val normalizedDataPoints = NormalizedSimpleLineChartData(originalDataPoints, customDataRange = null)

        normalizedDataPoints.normalizedYAxisValues.shouldBeEmpty()
        normalizedDataPoints.dataRange shouldBeEqualTo 0.0..1.0
    }

    @Test
    fun `original data points are normalized to percentages according to given custom data range`() {
        val originalDataPoints = listOf(
            SimpleLineChartDataPoint(yAxisValue = 90.0, null),
            SimpleLineChartDataPoint(yAxisValue = 89.0, null),
            SimpleLineChartDataPoint(yAxisValue = 90.0, null),
            SimpleLineChartDataPoint(yAxisValue = 92.0, null),
            SimpleLineChartDataPoint(yAxisValue = 94.0, null),
            SimpleLineChartDataPoint(yAxisValue = 91.5, null),
        )

        val normalizedDataPoints = NormalizedSimpleLineChartData(originalDataPoints, customDataRange = 85.0..95.0)


        normalizedDataPoints.normalizedYAxisValues[0] shouldBeEqualTo 0.5 // 90.0
        normalizedDataPoints.normalizedYAxisValues[1] shouldBeEqualTo 0.4 // 89.0
        normalizedDataPoints.normalizedYAxisValues[2] shouldBeEqualTo 0.5 // 90.0
        normalizedDataPoints.normalizedYAxisValues[3] shouldBeEqualTo 0.7 // 92.0
        normalizedDataPoints.normalizedYAxisValues[4] shouldBeEqualTo 0.9 // 94.0
        normalizedDataPoints.normalizedYAxisValues[5] shouldBeEqualTo 0.65 // 91.5
    }

}