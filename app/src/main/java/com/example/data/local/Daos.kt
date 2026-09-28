package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RideDao {
    @Query("SELECT * FROM rides ORDER BY timestamp DESC")
    fun getAllRides(): Flow<List<RideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: RideEntity): Long

    @Query("DELETE FROM rides WHERE id = :id")
    suspend fun deleteRide(id: Long)

    @Query("DELETE FROM rides")
    suspend fun clearAll()
}

@Dao
interface SavedPlaceDao {
    @Query("SELECT * FROM saved_places ORDER BY id ASC")
    fun getAllSavedPlaces(): Flow<List<SavedPlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: SavedPlaceEntity): Long

    @Query("DELETE FROM saved_places WHERE id = :id")
    suspend fun deletePlace(id: Long)
}
