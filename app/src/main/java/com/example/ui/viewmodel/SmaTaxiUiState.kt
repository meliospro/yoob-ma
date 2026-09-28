package com.example.ui.viewmodel

import com.example.data.local.RideEntity
import com.example.data.local.SavedPlaceEntity
import com.example.data.model.DriverInfo
import com.example.data.model.KaolackLocation
import com.example.data.model.KaolackPlaces
import com.example.data.model.MotoType
import com.example.data.model.PaymentMethod
import com.example.data.model.PaymentType
import com.example.data.model.RideStage

enum class MainTab {
    MAP,
    HISTORY,
    WALLET,
    GUIDE
}

data class ChatMessage(
    val sender: String,
    val text: String,
    val time: String,
    val isFromUser: Boolean
)

data class SmaTaxiUiState(
    val activeTab: MainTab = MainTab.MAP,
    val pickupLocation: KaolackLocation = KaolackPlaces.DEFAULT_PICKUP,
    val destinationLocation: KaolackLocation? = KaolackPlaces.DEFAULT_DESTINATION,
    val selectedMoto: MotoType = MotoType.ALL.first(),
    val selectedPayment: PaymentMethod = PaymentMethod.ALL.first(),
    val rideStage: RideStage = RideStage.MOTO_SELECTION,
    val currentDriver: DriverInfo? = null,
    val driverProgress: Float = 0f, // 0.0 to 1.0
    val driverX: Float = 0.5f,
    val driverY: Float = 0.5f,
    val estimatedArrivalMin: Int = 3,
    val tripDistanceKm: Float = 2.4f,
    val tripDurationMin: Int = 7,
    val tripFareFcfa: Int = 450,
    val walletBalanceFcfa: Int = 5000,
    val searchFilter: String = "",
    val isSelectingPickup: Boolean = false,
    val isDestinationPickerOpen: Boolean = false,
    val isSafetySheetOpen: Boolean = false,
    val isChatOpen: Boolean = false,
    val isRechargeModalOpen: Boolean = false,
    val isRatingModalOpen: Boolean = false,
    val chatMessages: List<ChatMessage> = emptyList(),
    val completedRating: Float = 5.0f,
    val tipAmountFcfa: Int = 0,
    val toastMessage: String? = null,
    val savedPlaces: List<SavedPlaceEntity> = emptyList(),
    val rideHistory: List<RideEntity> = emptyList()
)
