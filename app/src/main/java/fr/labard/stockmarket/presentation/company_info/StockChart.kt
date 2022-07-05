package fr.labard.stockmarket.presentation.company_info

import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.labard.stockmarket.domain.model.IntradayInfo
import kotlin.math.round
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    intradayInfos: List<IntradayInfo> = emptyList(),
    graphColor: Color = Color.Green
) {
    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val upperValue = remember(intradayInfos) {
        (intradayInfos.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0
    }
    val lowerValue = remember(intradayInfos) {
        intradayInfos.minOfOrNull { it.close }?.toInt() ?: 0
    }
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply { // use android.graphics.Paint instead of android.compose.ui.graphics
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / intradayInfos.size
        (0 until intradayInfos.size - 1 step 2).forEach { i ->
            val info = intradayInfos[i]
            val hour = info.date.hour
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "${hour}h",
                    spacing + i * spacePerHour,
                    size.height - 5,
                    textPaint
                )
            }
        }
        val priceStep = (upperValue - lowerValue) / 5f
        (1..5).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "${round(lowerValue + priceStep * i)}$",
                    30f,
                    size.height - i * size.height / 5f,
                    textPaint
                )
            }
        }
        var lastX = 0f
        var lastY: Float
        val strokePath = androidx.compose.ui.graphics.Path().apply {
            val height = size.height
            for (i in intradayInfos.indices) {
                val info = intradayInfos[i]
                val nextInfo = intradayInfos.getOrNull(i + 1) ?: intradayInfos.last()
                val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()
                if (i == 0) {
                    moveTo(x1, y1)
                }
                lastX = (x1 + x2) / 2f
                lastY = (y1 + y2) / 2f
                quadraticBezierTo(x1, y1, lastX, lastY)
            }
        }
        val fillPath = Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )
        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}