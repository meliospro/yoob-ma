package com.example.data.model

data class KaolackLocation(
    val id: String,
    val name: String,
    val neighborhood: String,
    val description: String,
    val category: LocationCategory,
    // Relative coordinates scaled for Kaolack canvas (0.0 to 1.0)
    val mapX: Float,
    val mapY: Float,
    val realLat: Double,
    val realLng: Double
)

enum class LocationCategory {
    MARKET,
    RELIGIOUS,
    TRANSPORT,
    HEALTH,
    EDUCATION,
    RESIDENTIAL,
    COMMERCIAL
}

object KaolackPlaces {
    val ALL = listOf(
        KaolackLocation(
            id = "marche_central",
            name = "Marché Central de Kaolack",
            neighborhood = "Centre-Ville",
            description = "Grand Marché couvert, allées des tissus, épices et quincaillerie",
            category = LocationCategory.MARKET,
            mapX = 0.50f,
            mapY = 0.52f,
            realLat = 14.1485,
            realLng = -16.0754
        ),
        KaolackLocation(
            id = "medina_baye",
            name = "Grande Mosquée Médina Baye",
            neighborhood = "Médina Baye",
            description = "Cité religieuse internationale de Cheikh Al Islam Ibrahima Niass",
            category = LocationCategory.RELIGIOUS,
            mapX = 0.68f,
            mapY = 0.35f,
            realLat = 14.1620,
            realLng = -16.0590
        ),
        KaolackLocation(
            id = "leona_niassene",
            name = "Léona Niassène",
            neighborhood = "Léona",
            description = "Quartier religieux et Zawiya El Hadj Abdoulaye Niass",
            category = LocationCategory.RELIGIOUS,
            mapX = 0.42f,
            mapY = 0.40f,
            realLat = 14.1560,
            realLng = -16.0820
        ),
        KaolackLocation(
            id = "garage_dakar",
            name = "Garage Dakar (Gare Routière)",
            neighborhood = "Ndorong Nord",
            description = "Point de départ vers Dakar, Thiès, Tamba et la Gambie",
            category = LocationCategory.TRANSPORT,
            mapX = 0.35f,
            mapY = 0.28f,
            realLat = 14.1680,
            realLng = -16.0890
        ),
        KaolackLocation(
            id = "hopital_regional",
            name = "Hôpital Régional El Hadj Ibrahima Niass",
            neighborhood = "Bongré",
            description = "Principal centre hospitalier régional de Kaolack",
            category = LocationCategory.HEALTH,
            mapX = 0.58f,
            mapY = 0.45f,
            realLat = 14.1520,
            realLng = -16.0680
        ),
        KaolackLocation(
            id = "coeur_de_ville",
            name = "Cœur de Ville Kaolack",
            neighborhood = "Centre-Ville",
            description = "Centre commercial moderne, agences Wave, boutiques",
            category = LocationCategory.COMMERCIAL,
            mapX = 0.48f,
            mapY = 0.56f,
            realLat = 14.1450,
            realLng = -16.0770
        ),
        KaolackLocation(
            id = "ussein",
            name = "Université USSEIN (Sing-Sing)",
            neighborhood = "Sing-Sing",
            description = "Université du Sine Saloum El Hadj Ibrahima Niass",
            category = LocationCategory.EDUCATION,
            mapX = 0.82f,
            mapY = 0.22f,
            realLat = 14.1750,
            realLng = -16.0400
        ),
        KaolackLocation(
            id = "ndorong",
            name = "Ndorong Rond-Point",
            neighborhood = "Ndorong",
            description = "Grand carrefour commercial et quartier populaire",
            category = LocationCategory.RESIDENTIAL,
            mapX = 0.32f,
            mapY = 0.38f,
            realLat = 14.1600,
            realLng = -16.0940
        ),
        KaolackLocation(
            id = "sara_nimzatt",
            name = "Sara Nimzatt",
            neighborhood = "Sara",
            description = "Quartier nord dynamique, écoles et commerces",
            category = LocationCategory.RESIDENTIAL,
            mapX = 0.45f,
            mapY = 0.20f,
            realLat = 14.1720,
            realLng = -16.0790
        ),
        KaolackLocation(
            id = "bongre",
            name = "Bongré Allées",
            neighborhood = "Bongré",
            description = "Zone résidentielle calme et institutions administratives",
            category = LocationCategory.RESIDENTIAL,
            mapX = 0.62f,
            mapY = 0.50f,
            realLat = 14.1490,
            realLng = -16.0640
        ),
        KaolackLocation(
            id = "port_kaolack",
            name = "Port Fluvial Saloum",
            neighborhood = "Quai Saloum",
            description = "Berges du Saloum, quai marchand et pêcheurs",
            category = LocationCategory.COMMERCIAL,
            mapX = 0.38f,
            mapY = 0.72f,
            realLat = 14.1350,
            realLng = -16.0860
        ),
        KaolackLocation(
            id = "dialegne",
            name = "Dialègne Centre",
            neighborhood = "Dialègne",
            description = "Quartier animé proche de la route nationale N1",
            category = LocationCategory.RESIDENTIAL,
            mapX = 0.55f,
            mapY = 0.62f,
            realLat = 14.1410,
            realLng = -16.0700
        ),
        KaolackLocation(
            id = "kasnack",
            name = "Kasnack Rond-Point",
            neighborhood = "Kasnack",
            description = "Secteur est reliant la route de Kaffrine",
            category = LocationCategory.RESIDENTIAL,
            mapX = 0.76f,
            mapY = 0.58f,
            realLat = 14.1440,
            realLng = -16.0490
        )
    )

    val DEFAULT_PICKUP = ALL[0] // Marché Central
    val DEFAULT_DESTINATION = ALL[1] // Médina Baye
}
