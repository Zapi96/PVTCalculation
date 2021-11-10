package com.example.pvtcalculation.positions.data.local

import android.content.Context
import androidx.room.Room

object LocalDB {

    /**
     * static method that creates a reminder class and returns the DAO of the reminder
     */
    fun createPositionsDao(context: Context): PositionsDao{
        return Room.databaseBuilder(
            context.applicationContext,
            PositionsDatabase::class.java, "positions.db"
        ).build().positionsDao()
    }

}