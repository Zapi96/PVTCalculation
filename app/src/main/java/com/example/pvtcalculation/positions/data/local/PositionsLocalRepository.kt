package com.example.pvtcalculation.positions.data.local

import com.example.pvtcalculation.positions.data.PositionDataSource
import com.example.pvtcalculation.positions.data.dto.PositionDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.pvtcalculation.positions.data.dto.Result


class PositionsLocalRepository(
    private val positionsDao: PositionsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PositionDataSource {

    /**
     * Get the positions list from the local db
     * @return Result the holds a Success with all the positions or an Error object with the error message
     */
    override suspend fun getPositions(): Result<List<PositionDTO>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(positionsDao.getPositions())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }



    /**
     * Get a position by its id
     * @param id to be used to get the position
     * @return Result the holds a Success object with the position or an Error object with the error message
     */
    override suspend fun getPositions(id: String): Result<PositionDTO> = withContext(ioDispatcher) {
        try {
            val position = positionsDao.getPositionById(id)
            if (position != null) {
                return@withContext Result.Success(position)
            } else {
                return@withContext Result.Error("Position not found!")
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    /**
     * Insert a position in the db.
     * @param position the position to be inserted
     */
    override suspend fun savePosition(position: PositionDTO) {
        withContext(ioDispatcher) {
            positionsDao.savePosition(position)
        }
    }

    /**
     * Deletes all the positions in the db
     */
    override suspend fun deleteAllPositions() {
        withContext(ioDispatcher) {
            positionsDao.deleteAllPositions()
        }
    }
}