package com.example.pvtcalculation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class Satellite(
                     val satid: Long, val date:String,
                     val intDesignator: String, val satname: String,
                     val launchDate: String, val satlat: Double,
                     val satlng: Double, val satalt: Double) : Parcelable