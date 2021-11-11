package com.example.pvtcalculation.positions.positionslist

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pvtcalculation.Above
import com.example.pvtcalculation.base.BaseViewModel
import com.example.pvtcalculation.positions.data.PositionDataSource
import com.example.pvtcalculation.positions.data.dto.PositionDTO
import kotlinx.coroutines.launch
import kotlin.collections.List
import com.example.pvtcalculation.positions.data.dto.Result


class ListPositionsViewModel(app: Application, private val dataSource: PositionDataSource) : BaseViewModel(app) {

    // list that holds the position data to be displayed on the UI
    val positionsList = MutableLiveData<List<Above>>()


    fun loadLocations() {
        showLoading.value = true
        viewModelScope.launch {
            //interacting with the dataSource has to be through a coroutine
            val result = dataSource.getPositions()
            showLoading.postValue(false)
            when (result) {
                is Result.Success<*> -> {
                    val dataList = ArrayList<Above>()
                    dataList.addAll((result.data as List<PositionDTO>).map { position ->
                        //map the position data from the DB to the be ready to be displayed on the UI
                        Above(
                            position.id,
                            position.date,
                            position.satcount,
                            position.latitude,
                            position.longitude,
                            position.altitude
                        )
                    })
                    positionsList.value = dataList
                    Log.w("SUCCESS","Hola")
                }
                is Result.Error ->
                    showSnackBar.value = result.message

            }

            invalidateShowNoData()
        }
    }


    private fun invalidateShowNoData() {
        showNoData.value = positionsList.value == null || positionsList.value!!.isEmpty()
    }
}