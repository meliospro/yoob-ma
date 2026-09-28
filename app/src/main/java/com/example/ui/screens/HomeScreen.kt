package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TwoWheeler
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KaolackLocation
import com.example.data.model.MotoType
import com.example.data.model.PaymentMethod
import com.example.data.model.RideStage
import com.example.ui.components.ActiveRideSheet
import com.example.ui.components.KaolackInteractiveMap
import com.example.ui.components.MotoTypeSelector
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaRed
import com.example.ui.theme.SmaYellow
import com.example.ui.viewmodel.SmaTaxiUiState

@Composable
fun HomeScreen(
    uiState: SmaTaxiUiState,
    onOpenDestinationPicker: (isForPickup: Boolean) -> Unit,
    onSwapLocations: () -> Unit,
    onSelectMoto: (MotoType) -> Unit,
    onSelectPayment: (PaymentMethod) -> Unit,
    onOrderRide: () -> Unit,
    onCancelRide: () -> Unit,
    onOpenChat: () -> Unit,
    onOpenSafety: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        // 1. Interactive Kaolack Vector Map
        KaolackInteractiveMap(
            pickupLocation = uiState.pickupLocation,
            destinationLocation = uiState.destinationLocation,
            driverX = uiState.driverX,
            driverY = uiState.driverY,
            rideStage = uiState.rideStage,
            onMapLocationTapped = { loc ->
                // Map interaction
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Top Header & Route Selection Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // App Bar Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(SmaYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TwoWheeler,
                                contentDescription = null,
                                tint = SmaBlack,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sma Taxi",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = SmaBlack
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = "Kaolack 🇸🇳",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SmaBlack,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Safety Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier.clickable(onClick = onOpenSafety)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Sécurité",
                            tint = SmaRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sécurité",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SmaBlack
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pickup & Destination Address Card (Yango style)
            if (uiState.rideStage == RideStage.PLANNING || uiState.rideStage == RideStage.MOTO_SELECTION) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("route_address_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Pickup row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenDestinationPicker(true) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(SmaGreen)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Point de départ",
                                    fontSize = 11.sp,
                                    color = SmaMuted
                                )
                                Text(
                                    text = uiState.pickupLocation.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SmaBlack,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Divider with swap button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(Color(0xFFE2E8F0))
                            )
                            IconButton(
                                onClick = onSwapLocations,
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("swap_locations_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "Inverser",
                                    tint = SmaMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(Color(0xFFE2E8F0))
                            )
                        }

                        // Destination row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenDestinationPicker(false) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(SmaRed)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Destination",
                                    fontSize = 11.sp,
                                    color = SmaMuted
                                )
                                Text(
                                    text = uiState.destinationLocation?.name ?: "Choisir une destination...",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (uiState.destinationLocation != null) SmaBlack else SmaMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Bottom Booking or Active Ride Sheet
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            when (uiState.rideStage) {
                RideStage.PLANNING, RideStage.MOTO_SELECTION -> {
                    MotoTypeSelector(
                        selectedMoto = uiState.selectedMoto,
                        onMotoSelected = onSelectMoto,
                        distanceKm = uiState.tripDistanceKm,
                        selectedPayment = uiState.selectedPayment,
                        onPaymentSelected = onSelectPayment,
                        onOrderRide = onOrderRide
                    )
                }

                RideStage.SEARCHING_DRIVER, RideStage.DRIVER_ASSIGNED, RideStage.DRIVER_ARRIVED, RideStage.IN_PROGRESS -> {
                    ActiveRideSheet(
                        rideStage = uiState.rideStage,
                        driver = uiState.currentDriver,
                        pickup = uiState.pickupLocation,
                        destination = uiState.destinationLocation,
                        fareFcfa = uiState.tripFareFcfa,
                        estimatedMinutesRemaining = uiState.estimatedArrivalMin,
                        onOpenChat = onOpenChat,
                        onOpenSafety = onOpenSafety,
                        onCancelRide = onCancelRide
                    )
                }

                else -> Unit
            }
        }
    }
}
