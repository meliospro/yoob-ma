package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides")
data class RideEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pickupName: String,
    val pickupNeighborhood: String,
    val destinationName: String,
    val destinationNeighborhood: String,
    val motoCategory: String,
    val fareFcfa: Int,
    val distanceKm: Float,
    val durationMinutes: Int,
    val driverName: String,
    val driverPlate: String,
    val paymentType: String,
    val timestamp: Long = System.currentTimeMillis(),
    val rating: Float = 5.0f,
    val status: String = "COMPLETED"
)

@Entity(tableName = "saved_places")
data class SavedPlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val label: String,
    val name: String,
    val neighborhood: String,
    val mapX: Float,
    val mapY: Float,
    val iconType: String
)
