package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverInfo
import com.example.data.model.KaolackLocation
import com.example.data.model.RideStage
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaRed
import com.example.ui.theme.SmaYellow
import com.example.ui.theme.SmaYellowDark

@Composable
fun ActiveRideSheet(
    rideStage: RideStage,
    driver: DriverInfo?,
    pickup: KaolackLocation,
    destination: KaolackLocation?,
    fareFcfa: Int,
    estimatedMinutesRemaining: Int,
    onOpenChat: () -> Unit,
    onOpenSafety: () -> Unit,
    onCancelRide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("active_ride_sheet"),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFCBD5E1))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (rideStage) {
                RideStage.SEARCHING_DRIVER -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = SmaYellow,
                            strokeWidth = 4.dp,
                            modifier = Modifier
                                .size(52.dp)
                                .testTag("radar_searching_indicator")
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Recherche d'un conducteur Jakarta...",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SmaBlack
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Attribution au conducteur le plus proche à Kaolack",
                            fontSize = 13.sp,
                            color = SmaMuted
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedButton(
                            onClick = onCancelRide,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("cancel_search_button")
                        ) {
                            Text("Annuler la recherche", color = SmaRed)
                        }
                    }
                }

                RideStage.DRIVER_ASSIGNED, RideStage.DRIVER_ARRIVED, RideStage.IN_PROGRESS -> {
                    // Header status
                    val statusTitle = when (rideStage) {
                        RideStage.DRIVER_ASSIGNED -> "Conducteur en route ($estimatedMinutesRemaining min)"
                        RideStage.DRIVER_ARRIVED -> "Votre Jakarta est arrivé !"
                        RideStage.IN_PROGRESS -> "Course vers ${destination?.name ?: "destination"}"
                        else -> ""
                    }

                    val statusSubtitle = when (rideStage) {
                        RideStage.DRIVER_ASSIGNED -> "Rejoignez le point : ${pickup.name}"
                        RideStage.DRIVER_ARRIVED -> "Vérifiez la plaque et mettez votre casque"
                        RideStage.IN_PROGRESS -> "Arrivée estimée dans $estimatedMinutesRemaining min"
                        else -> ""
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = statusTitle,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = SmaBlack
                            )
                            Text(
                                text = statusSubtitle,
                                fontSize = 13.sp,
                                color = if (rideStage == RideStage.DRIVER_ARRIVED) SmaGreen else SmaMuted,
                                fontWeight = if (rideStage == RideStage.DRIVER_ARRIVED) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }

                        // Safety SOS button
                        IconButton(
                            onClick = onOpenSafety,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFFFEF2F2))
                                .testTag("safety_sos_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Sécurité & Urgences",
                                tint = SmaRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Linear progress indicator
                    val progressVal = when (rideStage) {
                        RideStage.DRIVER_ASSIGNED -> 0.35f
                        RideStage.DRIVER_ARRIVED -> 0.60f
                        RideStage.IN_PROGRESS -> 0.85f
                        else -> 0.1f
                    }
                    LinearProgressIndicator(
                        progress = { progressVal },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SmaYellow,
                        trackColor = Color(0xFFF1F5F9)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Driver info card
                    if (driver != null) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Driver avatar badge
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(SmaYellow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = SmaBlack,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = driver.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SmaBlack
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Chauffeur vérifié",
                                            tint = SmaGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = SmaYellowDark,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "${driver.rating} • ${driver.totalRides} courses",
                                            fontSize = 12.sp,
                                            color = SmaMuted
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = driver.motoModel,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = SmaBlack
                                    )
                                }

                                // License Plate Badge
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White,
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SmaBlack)
                                ) {
                                    Text(
                                        text = driver.plateNumber,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SmaBlack,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons: Call, Chat, Cancel
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${driver.phone}")
                                    }
                                    context.startActivity(dialIntent)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF1F5F9),
                                    contentColor = SmaBlack
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("call_driver_button")
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Appeler")
                            }

                            Button(
                                onClick = onOpenChat,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF1F5F9),
                                    contentColor = SmaBlack
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("chat_driver_button")
                            ) {
                                Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Message")
                            }

                            if (rideStage != RideStage.IN_PROGRESS) {
                                OutlinedButton(
                                    onClick = onCancelRide,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("cancel_active_ride_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Annuler", tint = SmaRed)
                                }
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}
