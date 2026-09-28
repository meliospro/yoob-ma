package com.example.data.model

data class DriverInfo(
    val id: String,
    val name: String,
    val phone: String,
    val motoModel: String,
    val plateNumber: String,
    val rating: Float,
    val totalRides: Int,
    val verified: Boolean = true,
    var currentX: Float,
    var currentY: Float
) {
    companion object {
        val KAOLACK_DRIVERS = listOf(
            DriverInfo(
                id = "drv_1",
                name = "Moussa Diouf",
                phone = "+221 77 412 89 30",
                motoModel = "Jakarta Lifan 110 Noir/Jaune",
                plateNumber = "KL-4821-B",
                rating = 4.92f,
                totalRides = 1420,
                currentX = 0.48f,
                currentY = 0.50f
            ),
            DriverInfo(
                id = "drv_2",
                name = "Modou Ndiaye",
                phone = "+221 78 630 15 44",
                motoModel = "Bajaj Boxer 150 Rouge",
                plateNumber = "KL-9103-C",
                rating = 4.88f,
                totalRides = 890,
                currentX = 0.53f,
                currentY = 0.49f
            ),
            DriverInfo(
                id = "drv_3",
                name = "Cheikh Tidiane Cissé",
                phone = "+221 76 521 78 92",
                motoModel = "Haojue Elegant 125 Bleu",
                plateNumber = "KL-3312-A",
                rating = 4.96f,
                totalRides = 2100,
                currentX = 0.65f,
                currentY = 0.38f
            ),
            DriverInfo(
                id = "drv_4",
                name = "Babacar Sarr",
                phone = "+221 77 890 44 11",
                motoModel = "TVS Star HLX 125 Jaune",
                plateNumber = "KL-6704-B",
                rating = 4.85f,
                totalRides = 640,
                currentX = 0.38f,
                currentY = 0.35f
            )
        )
    }
}
