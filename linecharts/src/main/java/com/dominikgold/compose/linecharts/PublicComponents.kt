package com.dominikgold.compose.linecharts

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import com.dominikgold.compose.linecharts.models.LineChartConfig
import com.dominikgold.compose.linecharts.models.SimpleLineChartConfig
import com.dominikgold.compose.linecharts.models.SimpleLineChartDataPoint
import com.dominikgold.compose.linecharts.models.SimpleLineChartState
import kotlin.math.roundToInt

@Composable
fun SimpleLineChart(
    lineChartState: SimpleLineChartState,
    modifier: Modifier = Modifier,
    config: LineChartConfig = SimpleLineChartConfig(),
    hoverPopup: @Composable ((SimpleLineChartDataPoint) -> Unit)? = null,
) {
    BoxWithConstraints(modifier) {
        val chartHeight = constraints.constrainHeight((constraints.maxWidth * 0.75).roundToInt())
        with(LocalDensity.current) {
            Row(Modifier.size(constraints.maxWidth.toDp(), chartHeight.toDp())) {
                config.yAxisLabelsText?.let { yAxisLabelsText ->
                    Column(horizontalAlignment = Alignment.End) {
                        yAxisLabelsText(lineChartState.currentDataPoints.dataRange.endInclusive)
                        Spacer(modifier = Modifier.weight(1f))
                        yAxisLabelsText(lineChartState.currentDataPoints.dataRange.start)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
                SimpleLineChartInternal(
                    lineChartState = lineChartState,
                    // TODO most likely no need to drag a modifier through all the composables
                    modifier = Modifier,
                    config = config,
                    hoverPopup = if (hoverPopup != null) {
                        { dataPointIndex -> hoverPopup(lineChartState.currentDataPoints.original[dataPointIndex]) }
                    } else {
                        null
                    },
                )
            }
        }
    }
}
