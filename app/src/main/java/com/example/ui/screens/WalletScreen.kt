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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.OrangeMoney
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaYellow
import com.example.ui.theme.WaveBlue

@Composable
fun WalletScreen(
    balanceFcfa: Int,
    isRechargeOpen: Boolean,
    onOpenRecharge: (Boolean) -> Unit,
    onConfirmRecharge: (amount: Int, method: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("wallet_screen")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Portefeuille Sma Pay",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = SmaBlack
        )
        Text(
            text = "Payez vos courses Jakarta sans problème de monnaie",
            fontSize = 13.sp,
            color = SmaMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Balance Card
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = SmaBlack),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Solde disponible", fontSize = 13.sp, color = Color(0xFF94A3B8))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SmaYellow.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "FCFA Sénégal",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SmaYellow,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$balanceFcfa FCFA",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onOpenRecharge(true) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SmaYellow,
                        contentColor = SmaBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("recharge_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Recharger via Wave ou Orange Money", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Moyens de paiement acceptés à Kaolack",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SmaBlack
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Wave card
        PaymentMethodCard(
            title = "Wave Sénégal",
            subtitle = "Paiement sans frais par scan ou numéro",
            badgeText = "Recommandé",
            iconColor = WaveBlue,
            onClick = { onOpenRecharge(true) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Orange Money card
        PaymentMethodCard(
            title = "Orange Money",
            subtitle = "Paiement direct sécurisé",
            badgeText = "Instantané",
            iconColor = OrangeMoney,
            onClick = { onOpenRecharge(true) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Cash card
        PaymentMethodCard(
            title = "Espèces (Cash)",
            subtitle = "Règlement direct en fin de course au conducteur Jakarta",
            badgeText = "Toujours actif",
            iconColor = SmaGreen,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Security assurance
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFF1F5F9),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = SmaGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Vos transactions sont sécurisées. Vous ne payez que le montant affiché, sans surprise.",
                    fontSize = 12.sp,
                    color = SmaBlack.copy(alpha = 0.8f)
                )
            }
        }
    }

    // Recharge Dialog
    if (isRechargeOpen) {
        RechargeDialog(
            onDismiss = { onOpenRecharge(false) },
            onConfirm = { amount, method ->
                onConfirmRecharge(amount, method)
            }
        )
    }
}

@Composable
fun PaymentMethodCard(
    title: String,
    subtitle: String,
    badgeText: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SmaBlack)
                Text(text = subtitle, fontSize = 12.sp, color = SmaMuted)
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFF1F5F9)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SmaBlack,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
fun RechargeDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, String) -> Unit
) {
    var selectedAmount by remember { mutableIntStateOf(2000) }
    var selectedMethod by remember { mutableStateOf("Wave") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Recharger Sma Pay", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text("Choisissez le montant à créditer :", fontSize = 13.sp, color = SmaMuted)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1000, 2000, 5000).forEach { amount ->
                        FilterChip(
                            selected = selectedAmount == amount,
                            onClick = { selectedAmount = amount },
                            label = { Text("$amount F") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SmaYellow,
                                selectedLabelColor = SmaBlack
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Méthode de recharge :", fontSize = 13.sp, color = SmaMuted)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedMethod == "Wave",
                        onClick = { selectedMethod = "Wave" },
                        label = { Text("Wave") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = WaveBlue,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedMethod == "Orange Money",
                        onClick = { selectedMethod = "Orange Money" },
                        label = { Text("Orange Money") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OrangeMoney,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedAmount, selectedMethod) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SmaYellow,
                    contentColor = SmaBlack
                )
            ) {
                Text("Recharger $selectedAmount FCFA", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = SmaMuted
                )
            ) {
                Text("Annuler")
            }
        }
    )
}
