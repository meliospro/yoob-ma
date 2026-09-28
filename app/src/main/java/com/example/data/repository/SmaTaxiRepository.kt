package com.example.data.repository

import com.example.data.local.RideDao
import com.example.data.local.RideEntity
import com.example.data.local.SavedPlaceDao
import com.example.data.local.SavedPlaceEntity
import com.example.data.model.KaolackLocation
import com.example.data.model.MotoType
import kotlinx.coroutines.flow.Flow
import kotlin.math.hypot
import kotlin.math.roundToInt

class SmaTaxiRepository(
    private val rideDao: RideDao,
    private val savedPlaceDao: SavedPlaceDao
) {
    val allRides: Flow<List<RideEntity>> = rideDao.getAllRides()
    val savedPlaces: Flow<List<SavedPlaceEntity>> = savedPlaceDao.getAllSavedPlaces()

    suspend fun saveRide(ride: RideEntity): Long {
        return rideDao.insertRide(ride)
    }

    suspend fun deleteRide(id: Long) {
        rideDao.deleteRide(id)
    }

    suspend fun addSavedPlace(place: SavedPlaceEntity): Long {
        return savedPlaceDao.insertPlace(place)
    }

    suspend fun removeSavedPlace(id: Long) {
        savedPlaceDao.deletePlace(id)
    }

    // Calculate approximate road distance between two Kaolack points
    fun calculateDistanceKm(from: KaolackLocation, to: KaolackLocation): Float {
        val dx = (from.mapX - to.mapX) * 8f // approx 8km width of urban Kaolack
        val dy = (from.mapY - to.mapY) * 6f // approx 6km height
        val straight = hypot(dx.toDouble(), dy.toDouble()).toFloat()
        // Street detour multiplier (~1.3x in city grid & sandy alleys)
        val streetDistance = (straight * 1.35f).coerceAtLeast(0.8f)
        return (streetDistance * 10).roundToInt() / 10f
    }

    fun calculateDurationMinutes(distanceKm: Float): Int {
        // Jakarta motos in Kaolack average 25 km/h in urban streets
        val minutes = (distanceKm / 25f * 60f).roundToInt() + 2 // +2 min pickup
        return minutes.coerceAtLeast(3)
    }

    fun calculateFare(distanceKm: Float, motoType: MotoType): Int {
        val raw = motoType.baseFareFcfa + (distanceKm * motoType.perKmRateFcfa).roundToInt()
        // Round to nearest 50 FCFA (standard in Senegal)
        val remainder = raw % 50
        return if (remainder >= 25) raw + (50 - remainder) else raw - remainder
    }
}
