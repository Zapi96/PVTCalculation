package com.example.pvtcalculation

import android.app.Application
import android.icu.text.Transliterator
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pvtcalculation.api.GNSSApi
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.pvtcalculation.base.BaseViewModel
import com.example.pvtcalculation.base.NavigationCommand
import com.example.pvtcalculation.positions.data.PositionDataSource
import com.example.pvtcalculation.positions.data.dto.PositionDTO
import kotlinx.coroutines.launch
import java.util.*


class ConfigurationDownloadViewModel(val app: Application, val dataSource: PositionDataSource) :
    BaseViewModel(app) {

    private var _day = MutableLiveData<String>()
    val day : LiveData<String>
        get() = _day

    private var _month = MutableLiveData<String>()
    val month : LiveData<String>
        get() = _month

    private var _year = MutableLiveData<String>()
    val year : LiveData<String>
        get() = _year

    private var _date = MutableLiveData<String>()
    val date : LiveData<String>
        get() = _date

    private var _latitude = MutableLiveData<String>()
    val latitude : LiveData<String>
        get() = _latitude

    private var _longitude = MutableLiveData<String>()
    val longitude : LiveData<String>
        get() = _longitude

    private var _altitude = MutableLiveData<String>()
    val altitude : LiveData<String>
        get() = _altitude

    private var _buttonCompute = MutableLiveData<Boolean>()
    val buttonCompute : LiveData<Boolean>
        get() = _buttonCompute



    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<String>()

    val satelliteList = mutableListOf<Satellite>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response


    val searchRadius = "70"
    val categoryId = "18"
    val APIKey = "AWZCKM-QARK7C-HS2A2A-4SUO"

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        _buttonCompute.value = false
        _latitude.value = "41.702"
        _longitude.value = "-76.014"
        _altitude.value = "0"
    }


    fun onButtonCompute(){
        _buttonCompute.value = true
    }

    fun onButtonComputeComplete(){
        _buttonCompute.value = false
    }

    fun setLatitude(s: Editable){
        _latitude.value = s.toString()
    }

    fun setLongitude(s: Editable){
        _longitude.value = s.toString()
    }

    fun setAltitude(s: Editable){
        _altitude.value = s.toString()
    }

    fun assignDate(d:Int,m:Int,y:Int){
        _day.value = d.toString()
        _month.value = m.toString()
        _year.value = y.toString()
        _date.value = d.toString() + "/" + (m + 1) + "/" + y
    }




    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    fun getSatellitess() {
        _response.value = "Hola"
        try{
            GNSSApi.retrofitService.getAbove(latitude.value.toString(),
                                             longitude.value.toString(),
                                             altitude.value.toString(),
                                             searchRadius,
                                             categoryId,
                                             APIKey).enqueue( object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = "Failure: " + t.message
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val info = JSONObject(response.body()!!).getJSONObject("info")
                    val satcount = info.getInt("satcount")

                    val above = Above(UUID.randomUUID().toString(),date.value.toString(),satcount,latitude.value.toString(),longitude.value.toString(),altitude.value.toString())

                    val aboveJSON = JSONObject(response.body()!!).getJSONArray("above")
                    for (i in 0 until aboveJSON.length()) {
                        val satelliteJson = aboveJSON.getJSONObject(i)
                        val satid = satelliteJson.getLong("satid")
                        val satname = satelliteJson.getString("satname")
                        val intDesignator = satelliteJson.getString("intDesignator")
                        val launchDate = satelliteJson.getString("launchDate")
                        val satLat = satelliteJson.getDouble("satlat")
                        val satLng = satelliteJson.getDouble("satlng")
                        val satAlt = satelliteJson.getDouble("satalt")

                        val satellite = Satellite(
                            satid,
                            date.toString(),
                            satname,
                            intDesignator,
                            launchDate,
                            satLat,
                            satLng,
                            satAlt)
                        satelliteList.add(satellite)
                    }
                    _response.value = "Success: ${satcount} satellites for this location"
                    validateAndSaveReminder(above)

                }

                /**
                 * Validate the entered data then saves the reminder data to the DataSource
                 */
                fun validateAndSaveReminder(reminderData: Above) {
                    if (validateEnteredData(reminderData)) {
                        savePosition(reminderData)
                    }
                }

                /**
                 * Save the reminder to the data source
                 */
                fun savePosition(positionData: Above) {
                    showLoading.value = true
                    viewModelScope.launch {
                        dataSource.savePosition(
                            PositionDTO(
                                positionData.id,
                                positionData.date,
                                positionData.satcount,
                                positionData.latitude,
                                positionData.longitude,
                                positionData.altitude
                            )
                        )
                        showLoading.value = false
                        showToast.value = app.getString(R.string.reminder_saved)
                        navigationCommand.value = NavigationCommand.Back
                    }
                }
            })
        }catch (e: Exception){
            _response.value = "error"
        }



    }


    /**
     * Validate the entered data and show error to the user if there's any invalid data
     */
    fun validateEnteredData(reminderData: Above): Boolean {
        if (reminderData.date.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_enter_date
            return false
        }

        if (reminderData.latitude.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_enter_latitude
            return false
        }

        if (reminderData.longitude.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_enter_longitude
            return false
        }

        if (reminderData.altitude.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_enter_altitude
            return false
        }
        return true
    }

}
