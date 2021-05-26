package com.dominikgold.compose.linecharts.previews

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dominikgold.compose.linecharts.SimpleLineChart
import com.dominikgold.compose.linecharts.models.SimpleLineChartConfig
import com.dominikgold.compose.linecharts.models.SimpleLineChartDataPoint
import com.dominikgold.compose.linecharts.models.SimpleLineChartState

@Preview(showBackground = true)
@Composable
fun SimpleLineChartPreview() {
    Box(
        Modifier
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        val lineChartState = remember { SimpleLineChartState() }
        lineChartState.updateDataPoints(
            listOf(
                SimpleLineChartDataPoint(yAxisValue = 90.0, "6 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 89.0, "5 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 91.0, "4 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 93.0, "3 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 90.0, "2 w ago"),
                SimpleLineChartDataPoint(yAxisValue = 92.0, "1 w ago"),
            )
        )
        SimpleLineChart(
            lineChartState = lineChartState,
            config = SimpleLineChartConfig(animationSpec = null)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedLineChartPreview() {
    val lineChartState = remember { SimpleLineChartState() }
    lineChartState.updateDataPoints(
        listOf(
            SimpleLineChartDataPoint(yAxisValue = 90.0, "6 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 89.0, "5 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 91.0, "4 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 93.0, "3 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 90.0, "2 w ago"),
            SimpleLineChartDataPoint(yAxisValue = 92.0, "1 w ago"),
        )
    )
    Column(
        Modifier
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        SimpleLineChart(lineChartState, config = SimpleLineChartConfig(animationSpec = tween(500)))
        Button(onClick = {
            lineChartState.updateDataPoints(
                listOf(
                    SimpleLineChartDataPoint(yAxisValue = 88.0, "6 w ago"),
                    SimpleLineChartDataPoint(yAxisValue = 90.0, "5 w ago"),
                    SimpleLineChartDataPoint(yAxisValue = 92.0, "4 w ago"),
                    SimpleLineChartDataPoint(yAxisValue = 89.0, "3 w ago"),
                    SimpleLineChartDataPoint(yAxisValue = 87.0, "2 w ago"),
                    SimpleLineChartDataPoint(yAxisValue = 91.0, "1 w ago"),
                )
            )
        }) {
            Text("Click me!")
        }
    }
}