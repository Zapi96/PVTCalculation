package com.example.pvtcalculation.positions.data

import com.example.pvtcalculation.positions.data.dto.PositionDTO
import com.example.pvtcalculation.positions.data.dto.Result

interface PositionDataSource {
    suspend fun getPositions(): Result<List<PositionDTO>>
    suspend fun savePosition(position: PositionDTO)
    suspend fun getPositions(id: String): Result<PositionDTO>
    suspend fun deleteAllPositions()
}