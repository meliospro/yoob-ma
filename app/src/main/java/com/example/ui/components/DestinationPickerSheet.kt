package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.local.SavedPlaceEntity
import com.example.data.model.KaolackLocation
import com.example.data.model.KaolackPlaces
import com.example.data.model.LocationCategory
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaGreen
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationPickerSheet(
    isSelectingPickup: Boolean,
    currentPickup: KaolackLocation,
    currentDestination: KaolackLocation?,
    savedPlaces: List<SavedPlaceEntity>,
    onLocationSelected: (KaolackLocation) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<LocationCategory?>(null) }

    val filteredLocations = remember(searchQuery, selectedCategory) {
        KaolackPlaces.ALL.filter { loc ->
            val matchesQuery = searchQuery.isBlank() ||
                    loc.name.contains(searchQuery, ignoreCase = true) ||
                    loc.neighborhood.contains(searchQuery, ignoreCase = true) ||
                    loc.description.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == null || loc.category == selectedCategory
            matchesQuery && matchesCategory
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier.testTag("destination_picker_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isSelectingPickup) "Lieu de départ (Kaolack)" else "Où allez-vous ?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SmaBlack
                    )
                    Text(
                        text = "Sélectionnez un quartier ou lieu emblématique",
                        fontSize = 13.sp,
                        color = SmaMuted
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_picker_button")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Rechercher : Marché, Médina Baye, Léona...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SmaBlack)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Effacer")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("location_search_input"),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Tous les lieux") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SmaYellow,
                            selectedLabelColor = SmaBlack
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = selectedCategory == LocationCategory.MARKET,
                        onClick = { selectedCategory = if (selectedCategory == LocationCategory.MARKET) null else LocationCategory.MARKET },
                        label = { Text("Marchés") },
                        leadingIcon = { Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedCategory == LocationCategory.RELIGIOUS,
                        onClick = { selectedCategory = if (selectedCategory == LocationCategory.RELIGIOUS) null else LocationCategory.RELIGIOUS },
                        label = { Text("Lieux Religieux") },
                        leadingIcon = { Icon(Icons.Default.Mosque, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedCategory == LocationCategory.TRANSPORT,
                        onClick = { selectedCategory = if (selectedCategory == LocationCategory.TRANSPORT) null else LocationCategory.TRANSPORT },
                        label = { Text("Gares & Transports") },
                        leadingIcon = { Icon(Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedCategory == LocationCategory.RESIDENTIAL,
                        onClick = { selectedCategory = if (selectedCategory == LocationCategory.RESIDENTIAL) null else LocationCategory.RESIDENTIAL },
                        label = { Text("Quartiers") },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Locations List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredLocations, key = { it.id }) { loc ->
                    val isCurrentSelection = if (isSelectingPickup) {
                        loc.id == currentPickup.id
                    } else {
                        loc.id == currentDestination?.id
                    }

                    LocationItemRow(
                        location = loc,
                        isSelected = isCurrentSelection,
                        onClick = {
                            onLocationSelected(loc)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LocationItemRow(
    location: KaolackLocation,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("location_item_${location.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SmaYellow.copy(alpha = 0.2f) else Color(0xFFF8FAFC)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconVector = when (location.category) {
                LocationCategory.MARKET -> Icons.Default.ShoppingBag
                LocationCategory.RELIGIOUS -> Icons.Default.Mosque
                LocationCategory.TRANSPORT -> Icons.Default.NearMe
                LocationCategory.RESIDENTIAL -> Icons.Default.Home
                LocationCategory.COMMERCIAL -> Icons.Default.Work
                else -> Icons.Default.Place
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SmaYellow else Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = SmaBlack,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SmaBlack
                )
                Text(
                    text = "${location.neighborhood} • ${location.description}",
                    fontSize = 12.sp,
                    color = SmaMuted,
                    maxLines = 1
                )
            }

            if (isSelected) {
                Text(
                    text = "Choisi",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SmaGreen
                )
            }
        }
    }
}
