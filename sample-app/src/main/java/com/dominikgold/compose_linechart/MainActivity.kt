package com.dominikgold.compose_linechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dominikgold.compose.linecharts.SimpleLineChart
import com.dominikgold.compose.linecharts.models.SimpleLineChartConfig
import com.dominikgold.compose.linecharts.models.rememberSimpleLineChartState
import com.dominikgold.compose_linechart.ui.theme.ComposeLineChartTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLineChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val lineChartState = rememberSimpleLineChartState()
    var lineChartData by remember { mutableStateOf(generateRandomizedData()) }
    lineChartState.updateDataPoints(lineChartData, customDataRange = 0.0..100.0)
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Compose line charts sample", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(24.dp))
        SimpleLineChart(
            lineChartState = lineChartState,
            config = SimpleLineChartConfig(yAxisLabelsText = {
                Text(it.roundToInt().toString(), style = MaterialTheme.typography.caption)
            }),
            hoverPopup = { dataPoint ->
                Card(elevation = 8.dp) {
                    Column(Modifier.padding(8.dp)) {
                        Text(text = dataPoint.yAxisValue.roundToInt().toString(), fontWeight = FontWeight.SemiBold)
                        dataPoint.description?.let { Text(text = it) }
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(modifier = Modifier.align(CenterHorizontally), onClick = { lineChartData = generateRandomizedData() }) {
            Text(text = "Update data!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeLineChartTheme {
        MainScreen()
    }
}