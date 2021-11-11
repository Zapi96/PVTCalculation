package com.example.pvtcalculation

//import androidx.lifecycle.ViewModelProvider
//import android.os.Bundle
//import android.text.InputType
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
import android.Manifest
import android.annotation.TargetApi
import com.example.pvtcalculation.databinding.ConfigurationDownloadFragmentBinding
//import com.example.pvtcalculation.databinding.ConfigurationLoadFragmentBinding
//import android.widget.DatePicker

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer



import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TaskStackBuilder.create
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.pvtcalculation.base.BaseFragment
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*



class ConfigurationDownloadFragment : BaseFragment() {

    private lateinit var binding: ConfigurationDownloadFragmentBinding


    override val _viewModel: ConfigurationDownloadViewModel by inject()

    private lateinit var picker: DatePickerDialog
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    private var dateAssigned = false
    private val BACKGROUND_PERMISSIONS_CODE = 2106
    private var permissionsGranted = false
    private val LOG_TAG = "SentLocation"

    val PERMISSION_WRITE = 0


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.configuration_download_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this


        binding.downloadViewModel = _viewModel

        val eText = binding.editTextDate
        eText.setInputType(InputType.TYPE_NULL)

        eText.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            _viewModel.assignDate(day,month,year)
            // date picker dialog
            picker = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth -> eText.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                year,
                month,
                day
            )
            picker.show()
            dateAssigned = true
        }

        checkPermissions()

        _viewModel.buttonCompute.observe(viewLifecycleOwner, Observer{button ->
            if (button){
                _viewModel.onButtonComputeComplete()
                if (dateAssigned){
                    _viewModel.getSatellitess()
                }else{
                    Toast.makeText(requireContext(), "Please introduce a date", Toast.LENGTH_SHORT).show()
                }
            }

        })

        _viewModel.buttonSave.observe(viewLifecycleOwner, Observer{button ->
            if (button){
                _viewModel.onButtonSaveComplete()
                _viewModel.validateAndSavePosition()
            }

        })

        _viewModel.buttonCurrentLocation.observe(viewLifecycleOwner, Observer{button ->
            if (button && permissionsGranted){
                _viewModel.onButtonCurrentLocationComplete()
                getLocation()
            }

        })

        return binding.root

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkPermissions() {
        val permissions = arrayListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        // Segundo plano para Android Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        val permissionsArray = permissions.toTypedArray()
        if (hasPermissions(permissionsArray)) {
            permissionsGranted = true
            // Los permisos ya fueron concedidos
        } else {
            requestPerm(permissionsArray)
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            return ContextCompat.checkSelfPermission(
                requireActivity(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun requestPerm(permissions: Array<String>) {

//        requestPermissions(
//            permissions,
//            BACKGROUND_PERMISSIONS_CODE
//        )
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }




        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == BACKGROUND_PERMISSIONS_CODE) {
            val allPermissionsGranted =
                grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (grantResults.isNotEmpty() && allPermissionsGranted) {
                permissionsGranted = true;
                Log.d(LOG_TAG, "All permissions granted by the user")
            } else {
                Log.d(LOG_TAG, "One or more permissions where denied")
            }
        }
    }

    fun saveLocation(location: Location) {
        _viewModel.setLocation(location.latitude,location.longitude,location.altitude)
        binding.editTextLatitude.setText(location.latitude.toString())
        binding.editTextLongitude.setText(location.longitude.toString())
        binding.editTextAltitude.setText(location.altitude.toString())
        Log.d(LOG_TAG, "Latitud es ${location.latitude} y la longitud es ${location.longitude}")
    }

    fun getLocation() {
        // Hasta aquí sabemos que los permisos ya están concedidos
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    saveLocation(it)
                } else {
                    Log.d(LOG_TAG, "Location could not be obtained")
                }
            }
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    Log.d(LOG_TAG, "Location update received")
                    for (location in locationResult.locations) {
                        saveLocation(location)

                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.d(LOG_TAG, "Try to check your permissions")
        }

    }




}


private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
private const val TAG = "DownloadFragment"
private const val LOCATION_PERMISSION_INDEX = 0
private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1


