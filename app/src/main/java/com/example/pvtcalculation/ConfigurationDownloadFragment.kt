package com.example.pvtcalculation

//import androidx.lifecycle.ViewModelProvider
//import android.os.Bundle
//import android.text.InputType
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
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
import android.text.InputType
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.pvtcalculation.base.BaseFragment
import org.koin.android.ext.android.inject
import java.util.*


class ConfigurationDownloadFragment : BaseFragment() {

    private lateinit var binding: ConfigurationDownloadFragmentBinding
//    private val viewModel: ConfigurationDownloadViewModel by lazy {
//        ViewModelProvider(this).get(ConfigurationDownloadViewModel::class.java)
//    }

    override val _viewModel: ConfigurationDownloadViewModel by inject()

    private lateinit var picker: DatePickerDialog
    private var dateAssigned = false


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

        return binding.root

    }



}