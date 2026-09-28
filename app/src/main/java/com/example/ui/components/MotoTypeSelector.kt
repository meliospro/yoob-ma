package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MotoCategory
import com.example.data.model.MotoType
import com.example.data.model.PaymentMethod
import com.example.data.model.PaymentType
import com.example.ui.theme.CashGreen
import com.example.ui.theme.OrangeMoney
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaDarkGray
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaYellow
import com.example.ui.theme.SmaYellowDark
import com.example.ui.theme.WaveBlue
import kotlin.math.roundToInt

@Composable
fun MotoTypeSelector(
    selectedMoto: MotoType,
    onMotoSelected: (MotoType) -> Unit,
    distanceKm: Float,
    selectedPayment: PaymentMethod,
    onPaymentSelected: (PaymentMethod) -> Unit,
    onOrderRide: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPaymentMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("moto_type_selector_card"),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Little top pill indicator
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFCBD5E1))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Moto Category Cards
            Text(
                text = "Choisissez votre moto Jakarta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SmaBlack
            )

            Spacer(modifier = Modifier.height(12.dp))

            MotoType.ALL.forEach { moto ->
                val isSelected = moto.category == selectedMoto.category
                val rawFare = moto.baseFareFcfa + (distanceKm * moto.perKmRateFcfa).roundToInt()
                val remainder = rawFare % 50
                val estimatedFare = if (remainder >= 25) rawFare + (50 - remainder) else rawFare - remainder

                MotoOptionRow(
                    moto = moto,
                    fareFcfa = estimatedFare,
                    isSelected = isSelected,
                    onClick = { onMotoSelected(moto) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Payment and Safety info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Payment Method pill button
                Box {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        color = Color(0xFFF8FAFC),
                        modifier = Modifier
                            .clickable { showPaymentMenu = true }
                            .testTag("select_payment_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val iconColor = when (selectedPayment.type) {
                                PaymentType.CASH -> CashGreen
                                PaymentType.WAVE -> WaveBlue
                                PaymentType.ORANGE_MONEY -> OrangeMoney
                                PaymentType.SMA_PAY -> SmaYellowDark
                            }
                            val iconVec = when (selectedPayment.type) {
                                PaymentType.CASH -> Icons.Default.Money
                                PaymentType.WAVE -> Icons.Default.PhoneAndroid
                                PaymentType.ORANGE_MONEY -> Icons.Default.PhoneAndroid
                                PaymentType.SMA_PAY -> Icons.Default.AccountBalanceWallet
                            }

                            Icon(
                                imageVector = iconVec,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedPayment.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SmaBlack
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = SmaMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showPaymentMenu,
                        onDismissRequest = { showPaymentMenu = false }
                    ) {
                        PaymentMethod.ALL.forEach { pm ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = pm.title, fontWeight = FontWeight.Medium)
                                        Text(text = pm.subtitle, fontSize = 11.sp, color = SmaMuted)
                                    }
                                },
                                onClick = {
                                    onPaymentSelected(pm)
                                    showPaymentMenu = false
                                }
                            )
                        }
                    }
                }

                // Helmet guarantee badge
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = SmaYellowDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Casque fourni",
                        fontSize = 12.sp,
                        color = SmaDarkGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Action Button
            Button(
                onClick = onOrderRide,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("order_sma_taxi_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SmaYellow,
                    contentColor = SmaBlack
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TwoWheeler,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Commander ${selectedMoto.title}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MotoOptionRow(
    moto: MotoType,
    fareFcfa: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("moto_option_${moto.category.name}"),
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) BorderStroke(2.dp, SmaYellow) else BorderStroke(1.dp, Color(0xFFE2E8F0)),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFFBEB) else Color(0xFFF8FAFC)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            val motoIcon = when (moto.category) {
                MotoCategory.EXPRESS -> Icons.Default.TwoWheeler
                MotoCategory.CONFORT -> Icons.Default.Moped
                MotoCategory.LIVRAISON -> Icons.Default.Inventory2
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SmaYellow else Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = motoIcon,
                    contentDescription = null,
                    tint = SmaBlack,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = moto.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SmaBlack
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SmaYellow.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = "${moto.estimatedArrivalMin} min",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SmaBlack,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = moto.subtitle,
                    fontSize = 12.sp,
                    color = SmaMuted
                )
            }

            // Fare
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$fareFcfa F",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SmaBlack
                )
                Text(
                    text = "CFA",
                    fontSize = 11.sp,
                    color = SmaMuted
                )
            }
        }
    }
}
