package com.dominikgold.compose_linechart

import com.dominikgold.compose.linecharts.SimpleLineChartDataPoint
import kotlin.random.Random

fun generateRandomizedData(numberOfPoints: Int = 10, minValue: Double = 0.0, maxValue: Double = 100.0): List<SimpleLineChartDataPoint> {
    return List(numberOfPoints) { SimpleLineChartDataPoint(Random.nextDouble(from = minValue, until = maxValue)) }
}