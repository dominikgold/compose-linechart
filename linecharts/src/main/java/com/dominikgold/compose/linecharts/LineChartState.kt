package com.dominikgold.compose.linecharts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberSimpleLineChartState() = remember { SimpleLineChartState() }

class SimpleLineChartState {

    internal var previousDataPoints = NormalizedSimpleLineChartData(
        original = listOf(SimpleLineChartDataPoint(0.0)),
        padding = 0.1,
    )
        private set

    internal var currentDataPoints = previousDataPoints
        private set

    fun updateDataPoints(simpleDataPoints: List<SimpleLineChartDataPoint>) {
        previousDataPoints = currentDataPoints
        currentDataPoints = NormalizedSimpleLineChartData(simpleDataPoints, padding = 0.1)
    }

}
