package com.example.pvtcalculation


import android.Manifest
import android.annotation.SuppressLint
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

    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.Q

    val PERMISSION_WRITE = 0

    companion object {
        //TODO: Add Constant for Location request
        const val REQUEST_LOCATION_PERMISSION = 1
    }


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

            if (button){
//                checkPermissions()
                _viewModel.onButtonCurrentLocationComplete()

                getLocation()

            }

        })

        return binding.root

    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
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


    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkLocationPermissions()) {
            LocationServices.getFusedLocationProviderClient(requireContext())
                .lastLocation.addOnSuccessListener { location ->
                    if(location != null) {
                        saveLocation(location)
                    } else {
                        Toast.makeText(requireActivity(), "Location needs to be enabled", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }





}


private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
private const val TAG = "DownloadFragment"
private const val LOCATION_PERMISSION_INDEX = 0
private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1


