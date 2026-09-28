package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.RideStage
import com.example.ui.components.ChatModal
import com.example.ui.components.DestinationPickerSheet
import com.example.ui.components.RideCompletedDialog
import com.example.ui.components.SafetyBottomSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.KaolackGuideScreen
import com.example.ui.screens.RideHistoryScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.SmaBlack
import com.example.ui.theme.SmaMuted
import com.example.ui.theme.SmaTaxiTheme
import com.example.ui.theme.SmaYellow
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.SmaTaxiViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SmaTaxiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmaTaxiTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val context = LocalContext.current
                val snackbarHostState = remember { SnackbarHostState() }

                // Display toast or snackbar messages
                LaunchedEffect(uiState.toastMessage) {
                    uiState.toastMessage?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        viewModel.clearToast()
                    }
                }

                // Handle back press gracefully
                BackHandler(enabled = uiState.activeTab != MainTab.MAP || uiState.rideStage != RideStage.MOTO_SELECTION) {
                    if (uiState.activeTab != MainTab.MAP) {
                        viewModel.selectTab(MainTab.MAP)
                    } else if (uiState.rideStage == RideStage.SEARCHING_DRIVER) {
                        viewModel.cancelRide()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        // Do not show bottom nav bar while actively searching or riding for immersive Yango UX
                        val isRideOngoing = uiState.rideStage in listOf(
                            RideStage.SEARCHING_DRIVER,
                            RideStage.DRIVER_ASSIGNED,
                            RideStage.DRIVER_ARRIVED,
                            RideStage.IN_PROGRESS
                        )

                        if (!isRideOngoing) {
                            NavigationBar(
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .testTag("main_navigation_bar"),
                                containerColor = Color.White,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = uiState.activeTab == MainTab.MAP,
                                    onClick = { viewModel.selectTab(MainTab.MAP) },
                                    icon = {
                                        Icon(
                                            imageVector = if (uiState.activeTab == MainTab.MAP) Icons.Filled.TwoWheeler else Icons.Outlined.TwoWheeler,
                                            contentDescription = "Commander"
                                        )
                                    },
                                    label = { Text("Commander", fontWeight = if (uiState.activeTab == MainTab.MAP) FontWeight.Bold else FontWeight.Normal) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = SmaBlack,
                                        selectedTextColor = SmaBlack,
                                        indicatorColor = SmaYellow,
                                        unselectedIconColor = SmaMuted,
                                        unselectedTextColor = SmaMuted
                                    ),
                                    modifier = Modifier.testTag("nav_tab_commander")
                                )

                                NavigationBarItem(
                                    selected = uiState.activeTab == MainTab.HISTORY,
                                    onClick = { viewModel.selectTab(MainTab.HISTORY) },
                                    icon = {
                                        Icon(
                                            imageVector = if (uiState.activeTab == MainTab.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                                            contentDescription = "Courses"
                                        )
                                    },
                                    label = { Text("Courses", fontWeight = if (uiState.activeTab == MainTab.HISTORY) FontWeight.Bold else FontWeight.Normal) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = SmaBlack,
                                        selectedTextColor = SmaBlack,
                                        indicatorColor = SmaYellow,
                                        unselectedIconColor = SmaMuted,
                                        unselectedTextColor = SmaMuted
                                    ),
                                    modifier = Modifier.testTag("nav_tab_history")
                                )

                                NavigationBarItem(
                                    selected = uiState.activeTab == MainTab.WALLET,
                                    onClick = { viewModel.selectTab(MainTab.WALLET) },
                                    icon = {
                                        Icon(
                                            imageVector = if (uiState.activeTab == MainTab.WALLET) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                                            contentDescription = "Portefeuille"
                                        )
                                    },
                                    label = { Text("Sma Pay", fontWeight = if (uiState.activeTab == MainTab.WALLET) FontWeight.Bold else FontWeight.Normal) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = SmaBlack,
                                        selectedTextColor = SmaBlack,
                                        indicatorColor = SmaYellow,
                                        unselectedIconColor = SmaMuted,
                                        unselectedTextColor = SmaMuted
                                    ),
                                    modifier = Modifier.testTag("nav_tab_wallet")
                                )

                                NavigationBarItem(
                                    selected = uiState.activeTab == MainTab.GUIDE,
                                    onClick = { viewModel.selectTab(MainTab.GUIDE) },
                                    icon = {
                                        Icon(
                                            imageVector = if (uiState.activeTab == MainTab.GUIDE) Icons.Filled.Explore else Icons.Outlined.Explore,
                                            contentDescription = "Kaolack"
                                        )
                                    },
                                    label = { Text("Kaolack", fontWeight = if (uiState.activeTab == MainTab.GUIDE) FontWeight.Bold else FontWeight.Normal) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = SmaBlack,
                                        selectedTextColor = SmaBlack,
                                        indicatorColor = SmaYellow,
                                        unselectedIconColor = SmaMuted,
                                        unselectedTextColor = SmaMuted
                                    ),
                                    modifier = Modifier.testTag("nav_tab_guide")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (uiState.activeTab) {
                            MainTab.MAP -> HomeScreen(
                                uiState = uiState,
                                onOpenDestinationPicker = { isPickup -> viewModel.openDestinationPicker(isPickup) },
                                onSwapLocations = { viewModel.swapPickupAndDestination() },
                                onSelectMoto = { moto -> viewModel.selectMotoType(moto) },
                                onSelectPayment = { payment -> viewModel.selectPaymentMethod(payment) },
                                onOrderRide = { viewModel.requestRide() },
                                onCancelRide = { viewModel.cancelRide() },
                                onOpenChat = { viewModel.openChat(true) },
                                onOpenSafety = { viewModel.openSafetySheet(true) }
                            )

                            MainTab.HISTORY -> RideHistoryScreen(
                                rides = uiState.rideHistory,
                                onRebookRide = { ride -> viewModel.rebookRide(ride) },
                                onDeleteRide = { id -> viewModel.deleteRideHistory(id) }
                            )

                            MainTab.WALLET -> WalletScreen(
                                balanceFcfa = uiState.walletBalanceFcfa,
                                isRechargeOpen = uiState.isRechargeModalOpen,
                                onOpenRecharge = { open -> viewModel.openRechargeModal(open) },
                                onConfirmRecharge = { amount, method -> viewModel.rechargeWallet(amount, method) }
                            )

                            MainTab.GUIDE -> KaolackGuideScreen()
                        }
                    }
                }

                // 1. Destination / Pickup Picker Sheet
                if (uiState.isDestinationPickerOpen) {
                    DestinationPickerSheet(
                        isSelectingPickup = uiState.isSelectingPickup,
                        currentPickup = uiState.pickupLocation,
                        currentDestination = uiState.destinationLocation,
                        savedPlaces = uiState.savedPlaces,
                        onLocationSelected = { loc ->
                            if (uiState.isSelectingPickup) {
                                viewModel.selectPickup(loc)
                            } else {
                                viewModel.selectDestination(loc)
                            }
                        },
                        onDismiss = { viewModel.closeDestinationPicker() }
                    )
                }

                // 2. Safety & Kaolack Emergency Bottom Sheet
                if (uiState.isSafetySheetOpen) {
                    SafetyBottomSheet(
                        driverPlate = uiState.currentDriver?.plateNumber,
                        destinationName = uiState.destinationLocation?.name,
                        onDismiss = { viewModel.openSafetySheet(false) }
                    )
                }

                // 3. In-App Chat Modal with Jakarta Driver
                if (uiState.isChatOpen) {
                    ChatModal(
                        driver = uiState.currentDriver,
                        messages = uiState.chatMessages,
                        onSendMessage = { text -> viewModel.sendChatMessage(text) },
                        onDismiss = { viewModel.openChat(false) }
                    )
                }

                // 4. Ride Completed Dialog with Receipt & Rating
                if (uiState.isRatingModalOpen) {
                    RideCompletedDialog(
                        driver = uiState.currentDriver,
                        pickup = uiState.pickupLocation,
                        destination = uiState.destinationLocation,
                        fareFcfa = uiState.tripFareFcfa,
                        distanceKm = uiState.tripDistanceKm,
                        durationMin = uiState.tripDurationMin,
                        paymentMethodName = uiState.selectedPayment.title,
                        onDismissAndSubmit = { rating, tip ->
                            viewModel.submitRating(rating, tip)
                        }
                    )
                }
            }
        }
    }
}
