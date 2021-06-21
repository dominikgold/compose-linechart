package com.dominikgold.compose.linecharts.utils

internal fun interpolateBetweenYAxisData(
    originalYAxisData: List<Percentage>,
    targetYAxisData: List<Percentage>,
    progress: Float,
): List<Point> {
    val originalDataPoints = createPoints(yAxisValues = if (originalYAxisData.isEmpty()) listOf(0.5) else originalYAxisData)
    if (progress <= 0f) {
        return originalDataPoints
    }
    val targetDataPoints = createPoints(yAxisValues = if (targetYAxisData.isEmpty()) listOf(0.5) else targetYAxisData)
    if (originalYAxisData.size == targetYAxisData.size) {
        return originalDataPoints.mapIndexed { index, point ->
            Point(point.x, interpolateYAxisValue(point.y, targetYAxisData[index], progress))
        }
    }
    val dataPointsAtOriginalXAxisValues = originalDataPoints.findPointsAtXAxisValuesOnOther(
        otherDataPoints = targetDataPoints,
        progress = progress,
        reverseProgressCalculation = true,
    )
    val dataPointsAtTargetXAxisValues = targetDataPoints.filter { pointToFilter ->
        dataPointsAtOriginalXAxisValues.none {
            pointToFilter.x == it.x
        }
    }.findPointsAtXAxisValuesOnOther(
        otherDataPoints = originalDataPoints,
        progress = progress,
        reverseProgressCalculation = false,
    )
    return (dataPointsAtOriginalXAxisValues + dataPointsAtTargetXAxisValues).sortedBy { it.x }
}

private fun List<Point>.findPointsAtXAxisValuesOnOther(
    otherDataPoints: List<Point>,
    progress: Float,
    reverseProgressCalculation: Boolean,
): List<Point> {
    val otherDataPointsIterator = otherDataPoints.iterator()
    var currentOtherPoint = otherDataPointsIterator.next()
    var previousOtherPoint = currentOtherPoint
    return this.map { sourcePoint ->
        while (sourcePoint.x > currentOtherPoint.x) {
            previousOtherPoint = currentOtherPoint
            currentOtherPoint = otherDataPointsIterator.next()
        }
        val deltaToSourceXValue = sourcePoint.x - previousOtherPoint.x
        val deltaToOtherXValue = currentOtherPoint.x - previousOtherPoint.x
        val normalizedDeltaToSourceXValue =
            if (deltaToOtherXValue > 0.0) deltaToSourceXValue / deltaToOtherXValue else 0.0
        val deltaToOtherYValue = currentOtherPoint.y - previousOtherPoint.y
        val otherYValueAtSourceXValue = previousOtherPoint.y + deltaToOtherYValue * normalizedDeltaToSourceXValue
        return@map Point(
            x = sourcePoint.x,
            y = if (reverseProgressCalculation) {
                sourcePoint.y + (otherYValueAtSourceXValue - sourcePoint.y) * progress
            } else {
                otherYValueAtSourceXValue + (sourcePoint.y - otherYValueAtSourceXValue) * progress
            },
        )
    }
}

private fun interpolateYAxisValue(originalValue: Percentage, targetValue: Percentage, progress: Float): Percentage {
    val delta = targetValue - originalValue
    return originalValue + delta * progress
}
