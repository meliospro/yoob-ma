package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverInfo
import com.example.data.model.KaolackLocation
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaYellow
import com.example.ui.theme.SmaYellowDark

@Composable
fun RideCompletedDialog(
    driver: DriverInfo?,
    pickup: KaolackLocation,
    destination: KaolackLocation?,
    fareFcfa: Int,
    distanceKm: Float,
    durationMin: Int,
    paymentMethodName: String,
    onDismissAndSubmit: (rating: Float, tipFcfa: Int) -> Unit
) {
    var rating by remember { mutableFloatStateOf(5.0f) }
    var selectedTip by remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = { onDismissAndSubmit(rating, selectedTip) },
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ride_completed_dialog"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Badge
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(SmaGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = SmaGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Arrivé à bon port !",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SmaBlack
                )
                Text(
                    text = "Merci d'avoir voyagé avec Sma Taxi Kaolack",
                    fontSize = 13.sp,
                    color = SmaMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Receipt Breakdown Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8FAFC),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total de la course", fontSize = 14.sp, color = SmaMuted)
                            Text(
                                "$fareFcfa FCFA",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SmaBlack
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Paiement", fontSize = 13.sp, color = SmaMuted)
                            Text(paymentMethodName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SmaBlack)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Trajet", fontSize = 13.sp, color = SmaMuted)
                            Text("$distanceKm km • $durationMin min", fontSize = 13.sp, color = SmaBlack)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rating Stars
                Text(
                    text = "Notez votre conducteur Jakarta",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SmaBlack
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { starIndex ->
                        val isFilled = starIndex <= rating
                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.Star,
                            contentDescription = "Étoile $starIndex",
                            tint = if (isFilled) SmaYellowDark else Color(0xFFCBD5E1),
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { rating = starIndex.toFloat() }
                                .padding(2.dp)
                                .testTag("star_rating_$starIndex")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tip Options
                Text(
                    text = "Ajouter un pourboire (Sarax) ?",
                    fontSize = 13.sp,
                    color = SmaMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(0, 100, 200, 500).forEach { tip ->
                        FilterChip(
                            selected = selectedTip == tip,
                            onClick = { selectedTip = tip },
                            label = { Text(if (tip == 0) "Aucun" else "+$tip F") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SmaYellow,
                                selectedLabelColor = SmaBlack
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismissAndSubmit(rating, selectedTip) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SmaYellow,
                    contentColor = SmaBlack
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_rating_button")
            ) {
                Text(
                    text = if (selectedTip > 0) "Terminer (${fareFcfa + selectedTip} FCFA)" else "Terminer la course",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}
