package com.example.pvtcalculation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.pvtcalculation.databinding.ConfigurationLoadFragmentBinding

class ConfigurationLoadFragment : Fragment() {

    private lateinit var binding: ConfigurationLoadFragmentBinding

    companion object {
        fun newInstance() = ConfigurationLoadFragment()
    }

    private lateinit var viewModel: ConfigurationLoadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.configuration_load_fragment,
            container,
            false
        )


//        binding. = viewModel
//        binding.setLifecycleOwner(requireActivity())


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfigurationLoadViewModel::class.java)




    }

}