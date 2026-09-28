package com.example.data.model

enum class MotoCategory {
    EXPRESS,
    CONFORT,
    LIVRAISON
}

data class MotoType(
    val category: MotoCategory,
    val title: String,
    val subtitle: String,
    val description: String,
    val baseFareFcfa: Int,
    val perKmRateFcfa: Int,
    val estimatedArrivalMin: Int,
    val badgeText: String,
    val maxLuggageKg: Int = 10
) {
    companion object {
        val ALL = listOf(
            MotoType(
                category = MotoCategory.EXPRESS,
                title = "Sma Express",
                subtitle = "Jakarta 110cc classique",
                description = "La plus rapide à travers les ruelles de Kaolack. Casque fourni.",
                baseFareFcfa = 300,
                perKmRateFcfa = 150,
                estimatedArrivalMin = 3,
                badgeText = "Populaire",
                maxLuggageKg = 8
            ),
            MotoType(
                category = MotoCategory.CONFORT,
                title = "Sma Confort",
                subtitle = "Bajaj Boxer / Haojue 150cc",
                description = "Suspensions renforcées, assise spacieuse et conduite douce.",
                baseFareFcfa = 500,
                perKmRateFcfa = 200,
                estimatedArrivalMin = 5,
                badgeText = "Grand Confort",
                maxLuggageKg = 20
            ),
            MotoType(
                category = MotoCategory.LIVRAISON,
                title = "Sma Marché & Colis",
                subtitle = "Porte-bagages sanglé",
                description = "Idéal pour transporter vos emplettes du Grand Marché ou colis volumineux.",
                baseFareFcfa = 400,
                perKmRateFcfa = 180,
                estimatedArrivalMin = 4,
                badgeText = "Pratique",
                maxLuggageKg = 40
            )
        )
    }
}
