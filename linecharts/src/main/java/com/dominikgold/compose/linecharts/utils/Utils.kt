package com.dominikgold.compose.linecharts.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntRect
import kotlin.math.pow
import kotlin.math.sqrt

internal typealias Percentage = Double

internal typealias Index = Int

internal fun IntRect.fits(other: IntRect) =
    this.top >= other.top && this.bottom <= other.bottom && this.left >= other.left && this.right <= other.right

internal fun List<Offset>.findClosestTo(origin: Offset, minimumProximity: Float): Offset? {
    return this.mapNotNull { offset ->
        val distanceToOrigin = offset.distanceTo(origin)
        if (distanceToOrigin > minimumProximity) null else (offset to distanceToOrigin)
    }.minByOrNull { (_, distance) ->
        distance
    }?.first
}

internal fun Offset.distanceTo(other: Offset): Float {
    return sqrt((other.x - this.x).pow(2) + (other.y - this.y).pow(2))
}
