package com.dominikgold.compose.linecharts

internal data class Point(val x: Percentage, val y: Percentage)

internal fun createPoints(yAxisValues: List<Percentage>): List<Point> {
    return yAxisValues.mapIndexed { index, value ->
        Point(x = index.toDouble() / (yAxisValues.size - 1).toDouble(), y = value)
    }
}
