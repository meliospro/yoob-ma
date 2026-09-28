package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [RideEntity::class, SavedPlaceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmaTaxiDatabase : RoomDatabase() {
    abstract fun rideDao(): RideDao
    abstract fun savedPlaceDao(): SavedPlaceDao

    companion object {
        @Volatile
        private var INSTANCE: SmaTaxiDatabase? = null

        fun getInstance(context: Context): SmaTaxiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmaTaxiDatabase::class.java,
                    "sma_taxi_database"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed initial saved places & sample past ride
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            database.savedPlaceDao().insertPlace(
                                SavedPlaceEntity(
                                    label = "Maison",
                                    name = "Médina Baye Ouest",
                                    neighborhood = "Médina Baye",
                                    mapX = 0.68f,
                                    mapY = 0.35f,
                                    iconType = "home"
                                )
                            )
                            database.savedPlaceDao().insertPlace(
                                SavedPlaceEntity(
                                    label = "Marché",
                                    name = "Marché Central de Kaolack",
                                    neighborhood = "Centre-Ville",
                                    mapX = 0.50f,
                                    mapY = 0.52f,
                                    iconType = "cart"
                                )
                            )
                            database.savedPlaceDao().insertPlace(
                                SavedPlaceEntity(
                                    label = "Travail",
                                    name = "Cœur de Ville Kaolack",
                                    neighborhood = "Centre-Ville",
                                    mapX = 0.48f,
                                    mapY = 0.56f,
                                    iconType = "work"
                                )
                            )
                            database.rideDao().insertRide(
                                RideEntity(
                                    pickupName = "Garage Dakar",
                                    pickupNeighborhood = "Ndorong Nord",
                                    destinationName = "Marché Central de Kaolack",
                                    destinationNeighborhood = "Centre-Ville",
                                    motoCategory = "EXPRESS",
                                    fareFcfa = 450,
                                    distanceKm = 2.4f,
                                    durationMinutes = 6,
                                    driverName = "Moussa Diouf",
                                    driverPlate = "KL-4821-B",
                                    paymentType = "WAVE",
                                    timestamp = System.currentTimeMillis() - 86400000L,
                                    rating = 5.0f,
                                    status = "COMPLETED"
                                )
                            )
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
