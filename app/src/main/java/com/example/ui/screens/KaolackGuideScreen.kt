package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaDarkGray
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaYellow

@Composable
fun KaolackGuideScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("kaolack_guide_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Guide Moto & Kaolack",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SmaBlack
            )
            Text(
                text = "Conseils pratiques pour circuler en toute sécurité",
                fontSize = 13.sp,
                color = SmaMuted
            )
        }

        // Hero Banner image
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_kaolack_hero),
                        contentDescription = "Sma Taxi Kaolack Moto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xCC0F172A)),
                                    startY = 60f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SmaYellow
                        ) {
                            Text(
                                text = "Kaolack Moto-Taxi",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SmaBlack,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "La moto Jakarta, le cœur battant du Saloum",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Rules of thumb card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SmaYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = SmaBlack, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Règles de sécurité Sma Taxi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SmaBlack
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    GuideBullet(
                        title = "Port du casque systématique",
                        description = "Tous nos conducteurs mettent à votre disposition un casque propre homologué."
                    )
                    GuideBullet(
                        title = "Tarification transparente en FCFA",
                        description = "Fini les marchandages interminables au carrefour : le prix est calculé à l'avance."
                    )
                    GuideBullet(
                        title = "Prudence aux carrefours de la N1",
                        description = "Les motos Jakarta respectent les priorités sur l'Avenue Valdiodio et au Rond-Point Ndorong."
                    )
                }
            }
        }

        // Highlights of Kaolack
        item {
            Text(
                text = "Quartiers et pôles d'activité de Kaolack",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SmaBlack
            )
        }

        item {
            PlaceHighlightCard(
                title = "Marché Central de Kaolack",
                neighborhood = "Centre-Ville",
                description = "L'un des plus grands marchés couverts d'Afrique. Allées animées, tissus wax, épices et artisanat. Idéal avec Sma Marché & Colis pour vos sacs.",
                fareEstimate = "300 - 500 F"
            )
        }

        item {
            PlaceHighlightCard(
                title = "Médina Baye & Gamou",
                neighborhood = "Médina Baye",
                description = "Haut-lieu spirituel mondial fondé par Cheikh Ibrahima Niass. Forte affluence lors du Mawlid. Motos Jakarta disponibles 24h/24.",
                fareEstimate = "400 - 650 F"
            )
        }

        item {
            PlaceHighlightCard(
                title = "Léona Niassène",
                neighborhood = "Léona",
                description = "Quartier historique et religieux abritant le mausolée d'El Hadj Abdoulaye Niass.",
                fareEstimate = "350 - 500 F"
            )
        }

        item {
            PlaceHighlightCard(
                title = "Université USSEIN (Sing-Sing)",
                neighborhood = "Route Sing-Sing",
                description = "Campus moderne de l'Université du Sine Saloum. Trajet direct et rapide en Jakarta.",
                fareEstimate = "500 - 800 F"
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun GuideBullet(title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(SmaYellow)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SmaBlack)
            Text(description, fontSize = 12.sp, color = SmaMuted)
        }
    }
}

@Composable
fun PlaceHighlightCard(
    title: String,
    neighborhood: String,
    description: String,
    fareEstimate: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SmaBlack)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Text(
                        fareEstimate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SmaBlack,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(neighborhood, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SmaYellow)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, fontSize = 12.sp, color = SmaMuted)
        }
    }
}
