package com.dominikgold.compose.linecharts.hoverpopup

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import com.dominikgold.compose.linecharts.utils.fits

internal enum class HoverPopupGravity(val priority: Int) {

    TopLeft(priority = 0),
    Top(priority = 1),
    TopRight(priority = 2),
    BottomLeft(priority = 3),
    Left(priority = 4),
    Bottom(priority = 5),
    BottomRight(priority = 6),
    Right(priority = 7);

    companion object {

        fun forHoverPopup(
            popupPosition: IntOffset,
            popupWidth: Int,
            popupHeight: Int,
            containerWidth: Int,
            containerHeight: Int,
        ): HoverPopupGravity {
            val sortedGravities = values().sortedBy { it.priority }
            return sortedGravities.firstOrNull { gravity ->
                val popupRectangle = gravity.popupDimensions(popupPosition, popupWidth, popupHeight)
                return@firstOrNull popupRectangle.fits(IntRect(offset = IntOffset.Zero, size = IntSize(containerWidth, containerHeight)))
            } ?: TopLeft
        }

    }

}

internal fun HoverPopupGravity.popupDimensions(
    popupPosition: IntOffset,
    popupWidth: Int,
    popupHeight: Int,
): IntRect = when (this) {
    HoverPopupGravity.TopLeft -> IntRect(
        topLeft = popupPosition - IntOffset(popupWidth, popupHeight),
        bottomRight = popupPosition
    )
    HoverPopupGravity.Top -> IntRect(
        topLeft = popupPosition - IntOffset(popupWidth / 2, popupHeight),
        bottomRight = popupPosition + IntOffset(popupWidth / 2, 0)
    )
    HoverPopupGravity.TopRight -> IntRect(
        topLeft = popupPosition - IntOffset(0, popupHeight),
        bottomRight = popupPosition + IntOffset(popupWidth, 0)
    )
    HoverPopupGravity.BottomLeft -> IntRect(
        topLeft = popupPosition - IntOffset(popupWidth, 0),
        bottomRight = popupPosition + IntOffset(0, popupHeight)
    )
    HoverPopupGravity.Left -> IntRect(
        topLeft = popupPosition - IntOffset(popupWidth, popupHeight / 2),
        bottomRight = popupPosition + IntOffset(0, popupHeight / 2)
    )
    HoverPopupGravity.Bottom -> IntRect(
        topLeft = popupPosition - IntOffset(popupWidth / 2, 0),
        bottomRight = popupPosition + IntOffset(popupWidth / 2, popupHeight)
    )
    HoverPopupGravity.BottomRight -> IntRect(
        topLeft = popupPosition,
        bottomRight = popupPosition + IntOffset(popupWidth, popupHeight)
    )
    HoverPopupGravity.Right -> IntRect(
        topLeft = popupPosition - IntOffset(0, popupHeight / 2),
        bottomRight = popupPosition + IntOffset(popupWidth, popupHeight / 2)
    )
}
