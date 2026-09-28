package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.RideEntity
import com.example.data.local.SavedPlaceEntity
import com.example.data.local.SmaTaxiDatabase
import com.example.data.model.DriverInfo
import com.example.data.model.KaolackLocation
import com.example.data.model.KaolackPlaces
import com.example.data.model.MotoType
import com.example.data.model.PaymentMethod
import com.example.data.model.PaymentType
import com.example.data.model.RideStage
import com.example.data.repository.SmaTaxiRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmaTaxiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SmaTaxiRepository

    private val _uiState = MutableStateFlow(SmaTaxiUiState())
    val uiState: StateFlow<SmaTaxiUiState> = _uiState.asStateFlow()

    private var rideSimulationJob: Job? = null

    init {
        val database = SmaTaxiDatabase.getInstance(application)
        repository = SmaTaxiRepository(database.rideDao(), database.savedPlaceDao())

        // Observe Room data
        viewModelScope.launch {
            repository.allRides.collect { rides ->
                _uiState.update { it.copy(rideHistory = rides) }
            }
        }

        viewModelScope.launch {
            repository.savedPlaces.collect { places ->
                _uiState.update { it.copy(savedPlaces = places) }
            }
        }

        recalculateTripStats()
    }

    private fun recalculateTripStats() {
        val currentState = _uiState.value
        val pickup = currentState.pickupLocation
        val dest = currentState.destinationLocation ?: return
        val moto = currentState.selectedMoto

        val dist = repository.calculateDistanceKm(pickup, dest)
        val duration = repository.calculateDurationMinutes(dist)
        val fare = repository.calculateFare(dist, moto)

        _uiState.update {
            it.copy(
                tripDistanceKm = dist,
                tripDurationMin = duration,
                tripFareFcfa = fare
            )
        }
    }

    fun selectTab(tab: MainTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun selectPickup(location: KaolackLocation) {
        _uiState.update {
            it.copy(
                pickupLocation = location,
                isDestinationPickerOpen = false
            )
        }
        recalculateTripStats()
    }

    fun selectDestination(location: KaolackLocation) {
        _uiState.update {
            it.copy(
                destinationLocation = location,
                isDestinationPickerOpen = false,
                rideStage = RideStage.MOTO_SELECTION
            )
        }
        recalculateTripStats()
    }

    fun swapPickupAndDestination() {
        val currentDest = _uiState.value.destinationLocation ?: return
        val currentPickup = _uiState.value.pickupLocation
        _uiState.update {
            it.copy(
                pickupLocation = currentDest,
                destinationLocation = currentPickup
            )
        }
        recalculateTripStats()
    }

    fun selectMotoType(moto: MotoType) {
        _uiState.update { it.copy(selectedMoto = moto) }
        recalculateTripStats()
    }

    fun selectPaymentMethod(payment: PaymentMethod) {
        _uiState.update { it.copy(selectedPayment = payment) }
    }

    fun openDestinationPicker(isForPickup: Boolean) {
        _uiState.update {
            it.copy(
                isDestinationPickerOpen = true,
                isSelectingPickup = isForPickup,
                searchFilter = ""
            )
        }
    }

    fun closeDestinationPicker() {
        _uiState.update { it.copy(isDestinationPickerOpen = false) }
    }

    fun setSearchFilter(query: String) {
        _uiState.update { it.copy(searchFilter = query) }
    }

    fun openSafetySheet(open: Boolean) {
        _uiState.update { it.copy(isSafetySheetOpen = open) }
    }

    fun openChat(open: Boolean) {
        _uiState.update { it.copy(isChatOpen = open) }
    }

    fun openRechargeModal(open: Boolean) {
        _uiState.update { it.copy(isRechargeModalOpen = open) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun rechargeWallet(amount: Int, method: String) {
        _uiState.update {
            it.copy(
                walletBalanceFcfa = it.walletBalanceFcfa + amount,
                isRechargeModalOpen = false,
                toastMessage = "Recharge de $amount FCFA réussie via $method !"
            )
        }
    }

    // Start Ride Request & Simulation without websockets
    fun requestRide() {
        val state = _uiState.value
        val dest = state.destinationLocation ?: return

        // If paying with Sma Pay, verify balance
        if (state.selectedPayment.type == PaymentType.SMA_PAY && state.walletBalanceFcfa < state.tripFareFcfa) {
            _uiState.update {
                it.copy(toastMessage = "Solde Sma Pay insuffisant ! Rechargez ou choisissez Espèces/Wave.")
            }
            return
        }

        rideSimulationJob?.cancel()
        rideSimulationJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    rideStage = RideStage.SEARCHING_DRIVER,
                    toastMessage = "Recherche d'un conducteur Jakarta à Kaolack..."
                )
            }

            // Radar animation time
            delay(2800)

            // Select an available driver near pickup
            val driver = DriverInfo.KAOLACK_DRIVERS.random().copy(
                currentX = (state.pickupLocation.mapX + (Math.random().toFloat() - 0.5f) * 0.15f).coerceIn(0.1f, 0.9f),
                currentY = (state.pickupLocation.mapY + (Math.random().toFloat() - 0.5f) * 0.15f).coerceIn(0.1f, 0.9f)
            )

            val initialTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val welcomeMessages = listOf(
                ChatMessage(
                    sender = driver.name,
                    text = "Salam Alaykoum ! Mangi ñëw légui (J'arrive bientôt). J'ai un casque pour vous.",
                    time = initialTime,
                    isFromUser = false
                )
            )

            _uiState.update {
                it.copy(
                    rideStage = RideStage.DRIVER_ASSIGNED,
                    currentDriver = driver,
                    driverX = driver.currentX,
                    driverY = driver.currentY,
                    driverProgress = 0f,
                    estimatedArrivalMin = 2,
                    chatMessages = welcomeMessages,
                    toastMessage = "Jakarta trouvé ! ${driver.name} arrive (${driver.plateNumber})"
                )
            }

            // Phase 1: Driver approaching pickup (5 seconds animation)
            val driverStartX = driver.currentX
            val driverStartY = driver.currentY
            val pickupX = state.pickupLocation.mapX
            val pickupY = state.pickupLocation.mapY

            val steps1 = 20
            for (step in 1..steps1) {
                delay(250)
                val progress = step.toFloat() / steps1
                val curX = driverStartX + (pickupX - driverStartX) * progress
                val curY = driverStartY + (pickupY - driverStartY) * progress
                _uiState.update {
                    it.copy(
                        driverX = curX,
                        driverY = curY,
                        driverProgress = progress * 0.5f,
                        estimatedArrivalMin = if (progress > 0.6f) 1 else 2
                    )
                }
            }

            // Phase 2: Driver arrived at pickup
            _uiState.update {
                it.copy(
                    rideStage = RideStage.DRIVER_ARRIVED,
                    driverX = pickupX,
                    driverY = pickupY,
                    driverProgress = 0.5f,
                    toastMessage = "Votre moto-taxi est là ! Montez et mettez le casque."
                )
            }

            delay(3500)

            // Phase 3: In progress to destination
            _uiState.update {
                it.copy(
                    rideStage = RideStage.IN_PROGRESS,
                    toastMessage = "Course en cours vers ${dest.name}..."
                )
            }

            val destX = dest.mapX
            val destY = dest.mapY
            val steps2 = 30
            for (step in 1..steps2) {
                delay(260)
                val progress = step.toFloat() / steps2
                val curX = pickupX + (destX - pickupX) * progress
                val curY = pickupY + (destY - pickupY) * progress
                _uiState.update {
                    it.copy(
                        driverX = curX,
                        driverY = curY,
                        driverProgress = 0.5f + (progress * 0.5f),
                        estimatedArrivalMin = ((1f - progress) * (state.tripDurationMin.toFloat())).toInt().coerceAtLeast(1)
                    )
                }
            }

            // Phase 4: Completed
            val finalFare = state.tripFareFcfa
            var newBalance = state.walletBalanceFcfa
            if (state.selectedPayment.type == PaymentType.SMA_PAY) {
                newBalance = (newBalance - finalFare).coerceAtLeast(0)
            }

            // Save to Room Database
            val completedRide = RideEntity(
                pickupName = state.pickupLocation.name,
                pickupNeighborhood = state.pickupLocation.neighborhood,
                destinationName = dest.name,
                destinationNeighborhood = dest.neighborhood,
                motoCategory = state.selectedMoto.category.name,
                fareFcfa = finalFare,
                distanceKm = state.tripDistanceKm,
                durationMinutes = state.tripDurationMin,
                driverName = driver.name,
                driverPlate = driver.plateNumber,
                paymentType = state.selectedPayment.type.name,
                timestamp = System.currentTimeMillis(),
                rating = 5.0f,
                status = "COMPLETED"
            )
            repository.saveRide(completedRide)

            _uiState.update {
                it.copy(
                    rideStage = RideStage.COMPLETED,
                    driverX = destX,
                    driverY = destY,
                    driverProgress = 1.0f,
                    walletBalanceFcfa = newBalance,
                    isRatingModalOpen = true,
                    completedRating = 5.0f,
                    tipAmountFcfa = 0,
                    toastMessage = "Vous êtes arrivé à bon port à Kaolack !"
                )
            }
        }
    }

    fun cancelRide() {
        rideSimulationJob?.cancel()
        _uiState.update {
            it.copy(
                rideStage = RideStage.MOTO_SELECTION,
                currentDriver = null,
                driverProgress = 0f,
                toastMessage = "Course annulée."
            )
        }
    }

    fun submitRating(rating: Float, tipFcfa: Int) {
        viewModelScope.launch {
            val state = _uiState.value
            if (tipFcfa > 0 && state.walletBalanceFcfa >= tipFcfa) {
                _uiState.update {
                    it.copy(walletBalanceFcfa = it.walletBalanceFcfa - tipFcfa)
                }
            }

            _uiState.update {
                it.copy(
                    isRatingModalOpen = false,
                    rideStage = RideStage.MOTO_SELECTION,
                    currentDriver = null,
                    toastMessage = "Merci ! Votre avis a été enregistré pour la communauté Kaolackoise."
                )
            }
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage(
            sender = "Moi",
            text = text,
            time = time,
            isFromUser = true
        )

        _uiState.update {
            it.copy(chatMessages = it.chatMessages + userMsg)
        }

        // Driver simulated auto-reply
        viewModelScope.launch {
            delay(1200)
            val driver = _uiState.value.currentDriver
            val replies = listOf(
                "Waaw mangi ci yoon bi (Je suis en route) !",
                "Parfait, je klaxonne dès que je suis devant.",
                "Bien reçu, portez bien le casque quand je serai là !",
                "D'accord, je suis au niveau du virage."
            )
            val replyMsg = ChatMessage(
                sender = driver?.name ?: "Conducteur",
                text = replies.random(),
                time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                isFromUser = false
            )
            _uiState.update {
                it.copy(chatMessages = it.chatMessages + replyMsg)
            }
        }
    }

    fun rebookRide(ride: RideEntity) {
        val pickup = KaolackPlaces.ALL.find { it.name == ride.pickupName } ?: KaolackPlaces.DEFAULT_PICKUP
        val dest = KaolackPlaces.ALL.find { it.name == ride.destinationName } ?: KaolackPlaces.DEFAULT_DESTINATION
        val moto = MotoType.ALL.find { it.category.name == ride.motoCategory } ?: MotoType.ALL.first()
        val payment = PaymentMethod.ALL.find { it.type.name == ride.paymentType } ?: PaymentMethod.ALL.first()

        _uiState.update {
            it.copy(
                activeTab = MainTab.MAP,
                pickupLocation = pickup,
                destinationLocation = dest,
                selectedMoto = moto,
                selectedPayment = payment,
                rideStage = RideStage.MOTO_SELECTION,
                toastMessage = "Itinéraire réappliqué : ${pickup.neighborhood} ➜ ${dest.neighborhood}"
            )
        }
        recalculateTripStats()
    }

    fun deleteRideHistory(id: Long) {
        viewModelScope.launch {
            repository.deleteRide(id)
            _uiState.update { it.copy(toastMessage = "Course supprimée de l'historique.") }
        }
    }
}
