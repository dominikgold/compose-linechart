package com.dominikgold.compose_linechart

import kotlin.random.Random

fun generateRandomizedData(numberOfPoints: Int = 10, minValue: Double = 0.0, maxValue: Double = 100.0): List<Double> {
    return List(numberOfPoints) { Random.nextDouble(from = minValue, until = maxValue) }
}