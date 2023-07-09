package com.example.timerwithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timerwithcompose.ui.theme.TimerWithComposeTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0xFF101010),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    PrintTimer(
                        totalTime = 100L * 1000L,
                        activeColor = Color.Green,
                        inActiveColor = Color.DarkGray,
                        pointColor = Color(0xFF37B900),
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PrintTimer(
    totalTime: Long,
    activeColor: Color,
    inActiveColor: Color,
    pointColor: Color,
    strokeWidth: Dp = 5.dp,
    modifier: Modifier,
    initValue: Float = 1f
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var isRunning by remember {
        mutableStateOf(false)
    }
    // percentage
    var value by remember {
        mutableStateOf(initValue)
    }

    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    LaunchedEffect(key1 = currentTime, key2 = isRunning) {
        if (currentTime > 0 && isRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }


    }
    Box(contentAlignment = Alignment.Center, modifier = modifier.onSizeChanged {
        size = it
    }) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inActiveColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(),size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = activeColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(),size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            var center = Offset(size.width / 2f, size.height / 2f)
            var r = size.width / 2f
            var beta = (250f * value + 145f) * (PI / 180f).toFloat()
            var x = cos(beta) * r
            var y = sin(beta) * r

            drawPoints(
                listOf(Offset(center.x + x, center.y + y)),
                pointMode = PointMode.Points,
                color = pointColor,
                cap = StrokeCap.Round,
                strokeWidth = (strokeWidth * 3f).toPx()
            )
        }
        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Button(
            onClick = {
                if (currentTime <= 0) {
                    currentTime = totalTime
                    isRunning = true
                } else {
                    isRunning = !isRunning
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning || currentTime >= 0L) Color.Red
                else Color.Green
            )
        ) {
            Text(
                text = if (isRunning && currentTime >= 0) "Stop"
                else if (!isRunning && currentTime >= 0) "Start"
                else "Restart",
                color = Color.White
            )
        }
    }
}