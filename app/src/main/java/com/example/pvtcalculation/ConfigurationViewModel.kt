package com.example.pvtcalculation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfigurationViewModel : ViewModel() {

    private var _buttonLoad = MutableLiveData<Boolean>()
    val buttonLoad : LiveData<Boolean>
        get() = _buttonLoad

    private var _buttonDownload = MutableLiveData<Boolean>()
    val buttonDownload : LiveData<Boolean>
        get() = _buttonDownload

    init {
        _buttonLoad.value = false
        _buttonDownload.value = false
    }

    fun onButtonDownload(){
        _buttonDownload.value = true
    }

    fun onButtonDownloadComplete(){
        _buttonDownload.value = false
    }

    fun onButtonLoad(){
        _buttonLoad.value = true
    }

    fun onButtonLoadComplete(){
        _buttonLoad.value = false
    }
}