package com.example.tripplanner.data.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: TripDatabase? = null

    fun get(context: Context): TripDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                        context.applicationContext,
                        TripDatabase::class.java,
                        "trip_planner.db"
                    )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
        }
}
