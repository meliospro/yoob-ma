package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaRed
import com.example.ui.theme.SmaYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyBottomSheet(
    driverPlate: String?,
    destinationName: String?,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier.testTag("safety_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SmaRed.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = SmaRed)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Sécurité & Urgences Kaolack",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SmaBlack
                        )
                        Text(
                            text = "Votre sécurité est notre priorité absolue",
                            fontSize = 12.sp,
                            color = SmaMuted
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Helmet guarantee
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = SmaBlack,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Port du casque obligatoire",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SmaBlack
                        )
                        Text(
                            text = "Tout conducteur Jakarta de Sma Taxi est tenu de vous fournir un casque propre et sécurisé.",
                            fontSize = 12.sp,
                            color = SmaBlack.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Share Trip Button
            OutlinedButton(
                onClick = {
                    val shareText = "Je suis en moto-taxi Sma Taxi à Kaolack vers ${destinationName ?: "ma destination"}. Moto Jakarta immatriculée ${driverPlate ?: "KL-****"}."
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Partager mon trajet Sma Taxi"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("share_trip_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Partager mon trajet avec un proche")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Numéros d'urgence régionaux (Kaolack)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = SmaBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            EmergencyContactRow(
                title = "Sapeurs-Pompiers (Kaolack)",
                subtitle = "Urgences & Accidents de la route",
                phone = "18",
                onCall = {
                    val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18"))
                    context.startActivity(dial)
                }
            )

            EmergencyContactRow(
                title = "Commissariat Central de Kaolack",
                subtitle = "Police Nationale Kaolack",
                phone = "17",
                onCall = {
                    val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:17"))
                    context.startActivity(dial)
                }
            )

            EmergencyContactRow(
                title = "Hôpital Régional El Hadj Ibrahima Niass",
                subtitle = "Bongré, Kaolack",
                phone = "+221339411020",
                onCall = {
                    val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:339411020"))
                    context.startActivity(dial)
                }
            )
        }
    }
}

@Composable
fun EmergencyContactRow(
    title: String,
    subtitle: String,
    phone: String,
    onCall: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = SmaBlack)
                Text(text = subtitle, fontSize = 12.sp, color = SmaMuted)
            }
            Button(
                onClick = onCall,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SmaRed.copy(alpha = 0.12f),
                    contentColor = SmaRed
                ),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(phone, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
