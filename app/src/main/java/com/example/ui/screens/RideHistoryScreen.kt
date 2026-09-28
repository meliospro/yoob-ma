package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.RideEntity
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaRed
import com.example.ui.theme.SmaYellow
import com.example.ui.theme.SmaYellowDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RideHistoryScreen(
    rides: List<RideEntity>,
    onRebookRide: (RideEntity) -> Unit,
    onDeleteRide: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSpentFcfa = rides.sumOf { it.fareFcfa }
    val totalKm = (rides.sumOf { it.distanceKm.toDouble() } * 10).toInt() / 10.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("ride_history_screen")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = "Mes Courses à Kaolack",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = SmaBlack
        )
        Text(
            text = "Historique de vos trajets en moto Jakarta",
            fontSize = 13.sp,
            color = SmaMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stats summary card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SmaBlack),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total trajets", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    Text(
                        "${rides.size} courses",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp)
                        .background(Color(0xFF334155))
                )
                Column {
                    Text("Distance", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    Text(
                        "$totalKm km",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SmaYellow
                    )
                }
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp)
                        .background(Color(0xFF334155))
                )
                Column {
                    Text("Dépensé", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    Text(
                        "$totalSpentFcfa F",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (rides.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = SmaMuted,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aucune course enregistrée",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SmaBlack
                    )
                    Text(
                        text = "Vos trajets complétés apparaîtront ici",
                        fontSize = 13.sp,
                        color = SmaMuted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rides, key = { it.id }) { ride ->
                    RideHistoryCard(
                        ride = ride,
                        onRebook = { onRebookRide(ride) },
                        onDelete = { onDeleteRide(ride.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RideHistoryCard(
    ride: RideEntity,
    onRebook: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.FRENCH).format(Date(ride.timestamp))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ride_card_${ride.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top row: date & fare
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateStr,
                    fontSize = 12.sp,
                    color = SmaMuted
                )
                Text(
                    text = "${ride.fareFcfa} FCFA",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SmaBlack
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Origin & Destination
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SmaGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${ride.pickupName} (${ride.pickupNeighborhood})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SmaBlack
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SmaRed)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${ride.destinationName} (${ride.destinationNeighborhood})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SmaBlack
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Driver & Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.TwoWheeler, contentDescription = null, tint = SmaMuted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${ride.driverName} • ${ride.driverPlate}",
                        fontSize = 12.sp,
                        color = SmaMuted
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = SmaYellowDark, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${ride.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SmaBlack
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Actions: Rebook & Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onRebook,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SmaYellow.copy(alpha = 0.2f),
                        contentColor = SmaBlack
                    ),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reprendre ce trajet", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
