package com.example.pvtcalculation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.*

data class Above(val id: String = UUID.randomUUID().toString(),
                 val date:String?,
                 val satcount:Int?,
                 val latitude:String?,
                 val longitude:String?,
                 val altitude:String?) : Serializable