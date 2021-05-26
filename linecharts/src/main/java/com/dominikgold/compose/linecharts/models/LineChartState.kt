package com.dominikgold.compose.linecharts.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberSimpleLineChartState() = remember { SimpleLineChartState() }

class SimpleLineChartState {

    internal var previousDataPoints = NormalizedSimpleLineChartData(
        original = listOf(SimpleLineChartDataPoint(0.0)),
        customDataRange = null,
    )
        private set

    internal var currentDataPoints = previousDataPoints
        private set

    fun updateDataPoints(simpleDataPoints: List<SimpleLineChartDataPoint>, customDataRange: ClosedRange<Double>? = null) {
        previousDataPoints = currentDataPoints
        currentDataPoints = NormalizedSimpleLineChartData(simpleDataPoints, customDataRange)
    }

}
