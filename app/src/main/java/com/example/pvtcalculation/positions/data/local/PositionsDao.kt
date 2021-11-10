package com.example.pvtcalculation.positions.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pvtcalculation.positions.data.dto.PositionDTO


@Dao
interface PositionsDao {
    /**
     * @return all reminders.
     */
    @Query("SELECT * FROM positions")
    suspend fun getPositions(): List<PositionDTO>

    /**
     * @param reminderId the id of the reminder
     * @return the reminder object with the reminderId
     */
    @Query("SELECT * FROM positions where id = :positionId")
    suspend fun getPositionById(positionId: String): PositionDTO?

    /**
     * Insert a reminder in the database. If the reminder already exists, replace it.
     *
     * @param reminder the reminder to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePosition(reminder: PositionDTO)

    /**
     * Delete all reminders.
     */
    @Query("DELETE FROM positions")
    suspend fun deleteAllPositions()

}