package com.dominikgold.compose.linecharts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dominikgold.compose.linecharts.models.LineChartConfig
import com.dominikgold.compose.linecharts.models.SimpleLineChartConfig
import com.dominikgold.compose.linecharts.models.SimpleLineChartDataPoint
import com.dominikgold.compose.linecharts.models.SimpleLineChartState

@Composable
fun SimpleLineChart(
    lineChartState: SimpleLineChartState,
    modifier: Modifier = Modifier,
    config: LineChartConfig = SimpleLineChartConfig(),
    hoverPopup: @Composable ((SimpleLineChartDataPoint) -> Unit)? = null,
) {
    SimpleLineChartInternal(
        lineChartState = lineChartState,
        modifier = modifier,
        config = config,
        hoverPopup = if (hoverPopup != null) {
            { dataPointIndex -> hoverPopup(lineChartState.currentDataPoints.original[dataPointIndex]) }
        } else {
            null
        },
    )
}