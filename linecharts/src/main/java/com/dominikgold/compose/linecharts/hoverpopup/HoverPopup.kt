package com.dominikgold.compose.linecharts.hoverpopup

import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.dominikgold.compose.linecharts.utils.Index
import com.dominikgold.compose.linecharts.utils.findClosestTo
import kotlin.math.roundToInt

@Composable
internal fun HoverPopup(
    hoverPopup: @Composable () -> Unit,
    hoverPopupPosition: Offset,
    chartWidth: Int,
    chartHeight: Int,
) {
    Layout(
        modifier = Modifier.zIndex(100f),
        content = { hoverPopup() },
    ) { measurables, constraints ->
        require(measurables.size == 1) { "hoverPopup composable should have exactly one child" }
        val popupPositionIntOffset = IntOffset(hoverPopupPosition.x.roundToInt(), hoverPopupPosition.y.roundToInt())
        val placeable = measurables.first().measure(constraints)
        // TODO: At the moment, the hover popup position is calculated twice, once here when figuring out which gravity to use, and once
        //  when actually placing the popup. There should be a way to refactor this so that the calculation only happens once
        val gravity = HoverPopupGravity.forHoverPopup(
            popupPosition = popupPositionIntOffset,
            popupWidth = placeable.width,
            popupHeight = placeable.height,
            containerWidth = chartWidth,
            containerHeight = chartHeight,
        )
        layout(placeable.width, placeable.height) {
            val popupDimensions = gravity.popupDimensions(
                popupPosition = popupPositionIntOffset,
                popupWidth = placeable.width,
                popupHeight = placeable.height,
            )
            placeable.place(position = popupDimensions.topLeft)
        }
    }
}

internal fun Modifier.detectHoverPopup(dataPointOffsets: List<Offset>, onHoverPopupStateChanged: (HoverPopupState?) -> Unit) =
    pointerInput(key1 = dataPointOffsets) {
        awaitPointerEventScope {
            // TODO evaluate whether cancellation is handled correctly here
            while (true) {
                val down = awaitFirstDown()
                var currentlyHoveredPoint = dataPointOffsets.findClosestTo(down.position, minimumProximity = 80f)
                onHoverPopupStateChanged(currentlyHoveredPoint?.let { HoverPopupState(position = it) })
                var drag = awaitDragOrCancellation(down.id)
                while (drag != null) {
                    val closestPointToDrag = dataPointOffsets.findClosestTo(drag.position, minimumProximity = 80f)
                    if (currentlyHoveredPoint != closestPointToDrag) {
                        currentlyHoveredPoint = closestPointToDrag
                        onHoverPopupStateChanged(currentlyHoveredPoint?.let { HoverPopupState(position = it) })
                    }
                    drag = awaitDragOrCancellation(down.id)
                }
                onHoverPopupStateChanged(null)
            }
        }
    }
