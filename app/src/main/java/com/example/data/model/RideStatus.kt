package com.example.data.model

enum class PaymentType {
    CASH,
    WAVE,
    ORANGE_MONEY,
    SMA_PAY
}

data class PaymentMethod(
    val type: PaymentType,
    val title: String,
    val subtitle: String,
    val iconName: String
) {
    companion object {
        val ALL = listOf(
            PaymentMethod(
                type = PaymentType.CASH,
                title = "Espèces (Cash)",
                subtitle = "Paiement direct au chauffeur Jakarta",
                iconName = "cash"
            ),
            PaymentMethod(
                type = PaymentType.WAVE,
                title = "Wave Sénégal",
                subtitle = "Sans frais • QR code ou numéro",
                iconName = "wave"
            ),
            PaymentMethod(
                type = PaymentType.ORANGE_MONEY,
                title = "Orange Money",
                subtitle = "Paiement instantané sécurisé",
                iconName = "om"
            ),
            PaymentMethod(
                type = PaymentType.SMA_PAY,
                title = "Sma Pay (Solde)",
                subtitle = "Débité depuis votre portefeuille in-app",
                iconName = "wallet"
            )
        )
    }
}

enum class RideStage {
    PLANNING,            // Selecting pickup and destination
    MOTO_SELECTION,      // Selecting moto category & payment
    SEARCHING_DRIVER,    // Scanning nearby drivers with radar
    DRIVER_ASSIGNED,     // Driver found, heading to pickup
    DRIVER_ARRIVED,      // Driver waiting at pickup point
    IN_PROGRESS,         // Riding towards destination
    COMPLETED,           // Reached destination, rating & receipt
    CANCELLED            // Cancelled by user
}
