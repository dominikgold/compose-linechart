package com.dominikgold.compose_linechart

import com.dominikgold.compose.linecharts.models.SimpleLineChartDataPoint
import kotlin.random.Random

fun generateRandomizedData(
    numberOfPoints: Int = Random.nextInt(from = 6, until = 12),
    minValue: Double = 0.0,
    maxValue: Double = 100.0,
): List<SimpleLineChartDataPoint> {
    return List(numberOfPoints) { index ->
        SimpleLineChartDataPoint(
            yAxisValue = Random.nextDouble(from = minValue, until = maxValue),
            description = "Index $index"
        )
    }
}