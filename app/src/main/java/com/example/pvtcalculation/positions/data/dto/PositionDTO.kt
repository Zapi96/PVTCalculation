package com.example.pvtcalculation.positions.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "positions")
data class PositionDTO(
    @PrimaryKey
    @ColumnInfo(name = "id") var id:  String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "date") var date: String?,
    @ColumnInfo(name = "satcount") var satcount: Int?,
    @ColumnInfo(name = "latitude") var latitude: String?,
    @ColumnInfo(name = "longitude") var longitude: String?,
    @ColumnInfo(name = "altitude") val altitude: String?
)

