package com.example.pvtcalculation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {

    private var _buttonConfiguration = MutableLiveData<Boolean>()
    val buttonConfiguration : LiveData<Boolean>
        get() = _buttonConfiguration

    init {
        _buttonConfiguration.value = false
    }

    fun onButtonConfiguration(){
        _buttonConfiguration.value = true
    }

    fun onButtonConfigurationComplete(){
        _buttonConfiguration.value = false
    }
}