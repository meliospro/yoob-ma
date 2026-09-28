package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.KaolackLocation
import com.example.data.model.KaolackPlaces
import com.example.data.model.RideStage
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaDarkGray
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaRed
import com.example.ui.theme.SmaYellow
import com.example.ui.theme.SmaYellowDark
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun KaolackInteractiveMap(
    pickupLocation: KaolackLocation,
    destinationLocation: KaolackLocation?,
    driverX: Float,
    driverY: Float,
    rideStage: RideStage,
    onMapLocationTapped: (KaolackLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    // Transform gesture states for zoom & pan
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Pulse animation for active radar/searching
    val infiniteTransition = rememberInfiniteTransition(label = "map_anim")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 65f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    // Dash phase animation for route line
    val dashPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dash_phase"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.75f, 2.5f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
            .testTag("kaolack_interactive_map")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height

            // Center and scale matrix calculations
            val cx = canvasW / 2f + offsetX
            val cy = canvasH / 2f + offsetY

            fun toScreenX(normX: Float): Float = cx + (normX - 0.5f) * canvasW * scale
            fun toScreenY(normY: Float): Float = cy + (normY - 0.5f) * canvasH * scale

            // 1. Draw Saloum River / Bras de mer Kaolack (South-West)
            val riverPath = Path().apply {
                val startX = toScreenX(0.0f)
                val startY = toScreenY(0.65f)
                moveTo(startX, startY)
                cubicTo(
                    toScreenX(0.25f), toScreenY(0.70f),
                    toScreenX(0.40f), toScreenY(0.85f),
                    toScreenX(0.60f), toScreenY(0.98f)
                )
                lineTo(toScreenX(0.0f), toScreenY(1.0f))
                close()
            }
            drawPath(
                path = riverPath,
                color = Color(0xFFBAE6FD) // Gentle Saloum water blue
            )

            // 2. Draw Kaolack Urban City Blocks / Quartiers (soft background polygons)
            val blockPaintColor = Color(0xFFE2E8F0)
            drawRect(
                color = blockPaintColor,
                topLeft = Offset(toScreenX(0.20f), toScreenY(0.18f)),
                size = androidx.compose.ui.geometry.Size(canvasW * 0.65f * scale, canvasH * 0.65f * scale),
                style = Stroke(width = 1.5f)
            )

            // 3. Draw Major Road Network of Kaolack
            val roadColor = Color(0xFFCBD5E1)
            val primaryAvenueColor = Color(0xFFFFD54F) // National Road N1 Dakar-Kaolack

            // Secondary Street Grid
            val secondaryStroke = Stroke(width = 4.dp.toPx() * scale)
            val primaryStroke = Stroke(width = 8.dp.toPx() * scale)

            // Route Nationale N1 (West to East across Kaolack)
            val n1Path = Path().apply {
                moveTo(toScreenX(0.10f), toScreenY(0.25f)) // From Dakar / Kahone direction
                lineTo(toScreenX(0.35f), toScreenY(0.28f)) // Garage Dakar
                lineTo(toScreenX(0.50f), toScreenY(0.52f)) // Marché Central
                lineTo(toScreenX(0.76f), toScreenY(0.58f)) // Kasnack / Route Kaffrine
                lineTo(toScreenX(0.95f), toScreenY(0.62f))
            }
            // Outline
            drawPath(path = n1Path, color = Color(0xFF94A3B8), style = Stroke(width = 10.dp.toPx() * scale))
            drawPath(path = n1Path, color = primaryAvenueColor, style = primaryStroke)

            // Avenue Valdiodio Ndiaye / Route de Nioro (North to South-East)
            val nioroPath = Path().apply {
                moveTo(toScreenX(0.45f), toScreenY(0.15f)) // North Sara
                lineTo(toScreenX(0.50f), toScreenY(0.52f)) // Marché Central
                lineTo(toScreenX(0.48f), toScreenY(0.56f)) // Cœur de ville
                lineTo(toScreenX(0.55f), toScreenY(0.75f)) // Route Nioro du Rip
            }
            drawPath(path = nioroPath, color = roadColor, style = secondaryStroke)

            // Boulevard vers Médina Baye & USSEIN
            val medinaBayeRoad = Path().apply {
                moveTo(toScreenX(0.50f), toScreenY(0.52f))
                lineTo(toScreenX(0.68f), toScreenY(0.35f)) // Médina Baye
                lineTo(toScreenX(0.82f), toScreenY(0.22f)) // USSEIN Sing Sing
            }
            drawPath(path = medinaBayeRoad, color = roadColor, style = secondaryStroke)

            // Léona Niassène Connection
            val leonaRoad = Path().apply {
                moveTo(toScreenX(0.35f), toScreenY(0.28f))
                lineTo(toScreenX(0.42f), toScreenY(0.40f)) // Léona
                lineTo(toScreenX(0.50f), toScreenY(0.52f))
            }
            drawPath(path = leonaRoad, color = roadColor, style = secondaryStroke)

            // 4. Draw Other Idle Motos Jakarta around Kaolack (simulated traffic)
            val idleMotos = listOf(
                Pair(0.46f, 0.48f),
                Pair(0.52f, 0.54f),
                Pair(0.64f, 0.38f),
                Pair(0.38f, 0.30f),
                Pair(0.72f, 0.55f)
            )
            for ((mx, my) in idleMotos) {
                val scX = toScreenX(mx)
                val scY = toScreenY(my)
                drawCircle(color = SmaYellow, radius = 6.dp.toPx() * scale, center = Offset(scX, scY))
                drawCircle(color = SmaBlack, radius = 6.dp.toPx() * scale, center = Offset(scX, scY), style = Stroke(width = 1.5.dp.toPx()))
            }

            // 5. Draw Active Route between Pickup and Destination
            if (destinationLocation != null) {
                val pX = toScreenX(pickupLocation.mapX)
                val pY = toScreenY(pickupLocation.mapY)
                val dX = toScreenX(destinationLocation.mapX)
                val dY = toScreenY(destinationLocation.mapY)

                val routePath = Path().apply {
                    moveTo(pX, pY)
                    // Mid curved control point simulating Kaolack avenue turns
                    val midX = (pX + dX) / 2f + (dY - pY) * 0.15f
                    val midY = (pY + dY) / 2f - (dX - pX) * 0.15f
                    quadraticTo(midX, midY, dX, dY)
                }

                // Route halo
                drawPath(
                    path = routePath,
                    color = Color(0x330F172A),
                    style = Stroke(width = 10.dp.toPx() * scale)
                )

                // Animated animated dashed polyline
                drawPath(
                    path = routePath,
                    color = SmaBlack,
                    style = Stroke(
                        width = 5.dp.toPx() * scale,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(24f, 16f),
                            dashPhase
                        )
                    )
                )

                // Destination Pin
                drawCircle(
                    color = SmaRed.copy(alpha = 0.25f),
                    radius = 16.dp.toPx() * scale,
                    center = Offset(dX, dY)
                )
                drawCircle(
                    color = SmaRed,
                    radius = 9.dp.toPx() * scale,
                    center = Offset(dX, dY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx() * scale,
                    center = Offset(dX, dY)
                )
            }

            // 6. Draw Pickup Pin (Green) with pulse
            val pickX = toScreenX(pickupLocation.mapX)
            val pickY = toScreenY(pickupLocation.mapY)

            if (rideStage == RideStage.SEARCHING_DRIVER) {
                drawCircle(
                    color = SmaYellow.copy(alpha = pulseAlpha),
                    radius = pulseRadius * scale,
                    center = Offset(pickX, pickY)
                )
            }

            drawCircle(
                color = SmaGreen.copy(alpha = 0.25f),
                radius = 16.dp.toPx() * scale,
                center = Offset(pickX, pickY)
            )
            drawCircle(
                color = SmaGreen,
                radius = 9.dp.toPx() * scale,
                center = Offset(pickX, pickY)
            )
            drawCircle(
                color = Color.White,
                radius = 4.dp.toPx() * scale,
                center = Offset(pickX, pickY)
            )

            // 7. Draw Driver's Moto Jakarta if assigned or in progress
            if (rideStage in listOf(RideStage.DRIVER_ASSIGNED, RideStage.DRIVER_ARRIVED, RideStage.IN_PROGRESS)) {
                val drvScreenX = toScreenX(driverX)
                val drvScreenY = toScreenY(driverY)

                // Driver radar ripple
                drawCircle(
                    color = SmaYellowDark.copy(alpha = 0.35f),
                    radius = 24.dp.toPx() * scale,
                    center = Offset(drvScreenX, drvScreenY)
                )

                // Moto body circle
                drawCircle(
                    color = SmaBlack,
                    radius = 14.dp.toPx() * scale,
                    center = Offset(drvScreenX, drvScreenY)
                )
                drawCircle(
                    color = SmaYellow,
                    radius = 10.dp.toPx() * scale,
                    center = Offset(drvScreenX, drvScreenY)
                )
                // Black central dot / moto handlebar
                drawCircle(
                    color = SmaBlack,
                    radius = 3.5.dp.toPx() * scale,
                    center = Offset(drvScreenX, drvScreenY)
                )
            }
        }

        // Map Overlay controls: Zoom In, Zoom Out, Recenter
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 90.dp, end = 16.dp)
        ) {
            SmallFloatingActionButton(
                onClick = { scale = (scale * 1.25f).coerceAtMost(2.5f) },
                containerColor = Color.White,
                contentColor = SmaBlack,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("zoom_in_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Zoomer")
            }

            SmallFloatingActionButton(
                onClick = { scale = (scale / 1.25f).coerceAtLeast(0.75f) },
                containerColor = Color.White,
                contentColor = SmaBlack,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("zoom_out_button")
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Dézoomer")
            }

            SmallFloatingActionButton(
                onClick = {
                    scale = 1f
                    offsetX = 0f
                    offsetY = 0f
                },
                containerColor = Color.White,
                contentColor = SmaBlack,
                modifier = Modifier.testTag("recenter_button")
            ) {
                Icon(imageVector = Icons.Default.LocationSearching, contentDescription = "Recentrer Kaolack")
            }
        }
    }
}
