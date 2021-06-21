package com.dominikgold.compose.linecharts.utils

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNear
import org.junit.Test

class DataPointsInterpolationTest {

    private fun Point.shouldBeNear(other: Point, delta: Double) {
        this.x.shouldBeNear(other.x, delta)
        this.y.shouldBeNear(other.y, delta)
    }

    @Test
    fun `interpolating between two sets of y axis data of same size`() {
        val originalData = listOf(0.5, 0.82, 0.4, 0.5)
        val targetData = listOf(0.6, 0.5, 0.8, 0.4)

        val withZeroProgress = interpolateBetweenYAxisData(originalData, targetData, 0f)
        withZeroProgress[0].shouldBeNear(Point(0.0, 0.5), delta = 0.0001)
        withZeroProgress[1].shouldBeNear(Point(0.3333, 0.82), delta = 0.0001)
        withZeroProgress[2].shouldBeNear(Point(0.6666, 0.4), delta = 0.0001)
        withZeroProgress[3].shouldBeNear(Point(1.0, 0.5), delta = 0.0001)

        val withOneFourthProgress = interpolateBetweenYAxisData(originalData, targetData, 0.25f)
        withOneFourthProgress[0].shouldBeNear(Point(0.0, 0.525), delta = 0.0001)
        withOneFourthProgress[1].shouldBeNear(Point(0.3333, 0.74), delta = 0.0001)
        withOneFourthProgress[2].shouldBeNear(Point(0.6666, 0.5), delta = 0.0001)
        withOneFourthProgress[3].shouldBeNear(Point(1.0, 0.475), delta = 0.0001)

        val withHalfProgress = interpolateBetweenYAxisData(originalData, targetData, 0.5f)
        withHalfProgress[0].shouldBeNear(Point(0.0, 0.55), delta = 0.0001)
        withHalfProgress[1].shouldBeNear(Point(0.3333, 0.66), delta = 0.0001)
        withHalfProgress[2].shouldBeNear(Point(0.6666, 0.6), delta = 0.0001)
        withHalfProgress[3].shouldBeNear(Point(1.0, 0.45), delta = 0.0001)

        val withThreeFourthProgress = interpolateBetweenYAxisData(originalData, targetData, 0.75f)
        withThreeFourthProgress[0].shouldBeNear(Point(0.0, 0.575), delta = 0.0001)
        withThreeFourthProgress[1].shouldBeNear(Point(0.3333, 0.58), delta = 0.0001)
        withThreeFourthProgress[2].shouldBeNear(Point(0.6666, 0.7), delta = 0.0001)
        withThreeFourthProgress[3].shouldBeNear(Point(1.0, 0.425), delta = 0.0001)

        val withFullProgress = interpolateBetweenYAxisData(originalData, targetData, 1f)
        withFullProgress[0].shouldBeNear(Point(0.0, 0.6), delta = 0.0001)
        withFullProgress[1].shouldBeNear(Point(0.3333, 0.5), delta = 0.0001)
        withFullProgress[2].shouldBeNear(Point(0.6666, 0.8), delta = 0.0001)
        withFullProgress[3].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)
    }

    @Test
    fun `interpolating between two sets of y axis data with larger target data set`() {
        val originalData = listOf(0.5, 0.82, 0.4)
        val targetData = listOf(0.6, 0.5, 0.8, 0.4)

        val withZeroProgress = interpolateBetweenYAxisData(originalData, targetData, 0f)
        withZeroProgress[0].shouldBeNear(Point(0.0, 0.5), delta = 0.0001)
        withZeroProgress[1].shouldBeNear(Point(0.5, 0.82), delta = 0.0001)
        withZeroProgress[2].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)

        val withOneFourthProgress = interpolateBetweenYAxisData(originalData, targetData, 0.25f)
        withOneFourthProgress[0].shouldBeNear(Point(0.0, 0.525), delta = 0.0001)
        withOneFourthProgress[1].shouldBeNear(Point(0.3333, 0.66), delta = 0.0001)
        withOneFourthProgress[2].shouldBeNear(Point(0.5, 0.7775), delta = 0.0001)
        withOneFourthProgress[3].shouldBeNear(Point(0.6666, 0.7099), delta = 0.0001)
        withOneFourthProgress[4].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)

        val withHalfProgress = interpolateBetweenYAxisData(originalData, targetData, 0.5f)
        withHalfProgress[0].shouldBeNear(Point(0.0, 0.55), delta = 0.0001)
        withHalfProgress[1].shouldBeNear(Point(0.3333, 0.6066), delta = 0.0001)
        withHalfProgress[2].shouldBeNear(Point(0.5, 0.735), delta = 0.0001)
        withHalfProgress[3].shouldBeNear(Point(0.6666, 0.7399), delta = 0.0001)
        withHalfProgress[4].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)

        val withThreeFourthProgress = interpolateBetweenYAxisData(originalData, targetData, 0.75f)
        withThreeFourthProgress[0].shouldBeNear(Point(0.0, 0.575), delta = 0.0001)
        withThreeFourthProgress[1].shouldBeNear(Point(0.3333, 0.5533), delta = 0.0001)
        withThreeFourthProgress[2].shouldBeNear(Point(0.5, 0.6925), delta = 0.0001)
        withThreeFourthProgress[3].shouldBeNear(Point(0.6666, 0.7699), delta = 0.0001)
        withThreeFourthProgress[4].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)

        val withFullProgress = interpolateBetweenYAxisData(originalData, targetData, 1f)
        withFullProgress[0].shouldBeNear(Point(0.0, 0.6), delta = 0.0001)
        withFullProgress[1].shouldBeNear(Point(0.3333, 0.5), delta = 0.0001)
        withFullProgress[2].shouldBeNear(Point(0.5, 0.65), delta = 0.0001)
        withFullProgress[3].shouldBeNear(Point(0.6666, 0.8), delta = 0.0001)
        withFullProgress[4].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)
    }

    @Test
    fun `interpolating between two sets of y axis data with larger original data set`() {
        val originalData = listOf(0.6, 0.5, 0.8, 0.4)
        val targetData = listOf(0.5, 0.82, 0.4)

        val withZeroProgress = interpolateBetweenYAxisData(originalData, targetData, 0f)
        withZeroProgress[0].shouldBeNear(Point(0.0, 0.6), delta = 0.0001)
        withZeroProgress[1].shouldBeNear(Point(0.3333, 0.5), delta = 0.0001)
        withZeroProgress[2].shouldBeNear(Point(0.6666, 0.8), delta = 0.0001)
        withZeroProgress[3].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)

        val withOneFourthProgress = interpolateBetweenYAxisData(originalData, targetData, 0.25f)
        withOneFourthProgress[0].shouldBeNear(Point(0.0, 0.575), delta = 0.0001)
        withOneFourthProgress[1].shouldBeNear(Point(0.3333, 0.5533), delta = 0.0001)
        withOneFourthProgress[2].shouldBeNear(Point(0.5, 0.6925), delta = 0.0001)
        withOneFourthProgress[3].shouldBeNear(Point(0.6666, 0.7699), delta = 0.0001)
        withOneFourthProgress[4].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)

        val withHalfProgress = interpolateBetweenYAxisData(originalData, targetData, 0.5f)
        withHalfProgress[0].shouldBeNear(Point(0.0, 0.55), delta = 0.0001)
        withHalfProgress[1].shouldBeNear(Point(0.3333, 0.6066), delta = 0.0001)
        withHalfProgress[2].shouldBeNear(Point(0.5, 0.735), delta = 0.0001)
        withHalfProgress[3].shouldBeNear(Point(0.6666, 0.7399), delta = 0.0001)
        withHalfProgress[4].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)

        val withThreeFourthProgress = interpolateBetweenYAxisData(originalData, targetData, 0.75f)
        withThreeFourthProgress[0].shouldBeNear(Point(0.0, 0.525), delta = 0.0001)
        withThreeFourthProgress[1].shouldBeNear(Point(0.3333, 0.66), delta = 0.0001)
        withThreeFourthProgress[2].shouldBeNear(Point(0.5, 0.7775), delta = 0.0001)
        withThreeFourthProgress[3].shouldBeNear(Point(0.6666, 0.7099), delta = 0.0001)
        withThreeFourthProgress[4].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)

        val withFullProgress = interpolateBetweenYAxisData(originalData, targetData, 1f)
        withFullProgress[0].shouldBeNear(Point(0.0, 0.5), delta = 0.0001)
        withFullProgress[1].shouldBeNear(Point(0.3333, 0.7133), delta = 0.0001)
        withFullProgress[2].shouldBeNear(Point(0.5, 0.82), delta = 0.0001)
        withFullProgress[3].shouldBeNear(Point(0.6666, 0.6799), delta = 0.0001)
        withFullProgress[4].shouldBeNear(Point(1.0, 0.4), delta = 0.0001)
    }

    @Test
    fun `an empty previous data set is handled as if it contains a single point in the center of the y axis`() {
        val emptyData = listOf<Double>()
        val withSinglePointAtHalf = listOf(0.5)
        val targetData = listOf(0.5, 0.82, 0.4)

        val withZeroProgress = interpolateBetweenYAxisData(emptyData, targetData, 0f)
        withZeroProgress shouldBeEqualTo interpolateBetweenYAxisData(withSinglePointAtHalf, targetData, 0f)

        val withOneFourthProgress = interpolateBetweenYAxisData(emptyData, targetData, 0.25f)
        withOneFourthProgress shouldBeEqualTo interpolateBetweenYAxisData(withSinglePointAtHalf, targetData, 0.25f)

        val withHalfProgress = interpolateBetweenYAxisData(emptyData, targetData, 0.5f)
        withHalfProgress shouldBeEqualTo interpolateBetweenYAxisData(withSinglePointAtHalf, targetData, 0.5f)

        val withThreeFourthProgress = interpolateBetweenYAxisData(emptyData, targetData, 0.75f)
        withThreeFourthProgress shouldBeEqualTo interpolateBetweenYAxisData(withSinglePointAtHalf, targetData, 0.75f)

        val withFullProgress = interpolateBetweenYAxisData(emptyData, targetData, 1f)
        withFullProgress shouldBeEqualTo interpolateBetweenYAxisData(withSinglePointAtHalf, targetData, 1f)
    }

    @Test
    fun `an empty target data set is handled as if it contains a single point in the center of the y axis`() {
        val originalData = listOf(0.5, 0.82, 0.4)
        val emptyData = listOf<Double>()
        val withSinglePointAtHalf = listOf(0.5)

        val withZeroProgress = interpolateBetweenYAxisData(originalData, emptyData, 0f)
        withZeroProgress shouldBeEqualTo interpolateBetweenYAxisData(originalData, withSinglePointAtHalf, 0f)

        val withOneFourthProgress = interpolateBetweenYAxisData(originalData, emptyData, 0.25f)
        withOneFourthProgress shouldBeEqualTo interpolateBetweenYAxisData(originalData, withSinglePointAtHalf, 0.25f)

        val withHalfProgress = interpolateBetweenYAxisData(originalData, emptyData, 0.5f)
        withHalfProgress shouldBeEqualTo interpolateBetweenYAxisData(originalData, withSinglePointAtHalf, 0.5f)

        val withThreeFourthProgress = interpolateBetweenYAxisData(originalData, emptyData, 0.75f)
        withThreeFourthProgress shouldBeEqualTo interpolateBetweenYAxisData(originalData, withSinglePointAtHalf, 0.75f)

        val withFullProgress = interpolateBetweenYAxisData(originalData, emptyData, 1f)
        withFullProgress shouldBeEqualTo interpolateBetweenYAxisData(originalData, withSinglePointAtHalf, 1f)
    }

}