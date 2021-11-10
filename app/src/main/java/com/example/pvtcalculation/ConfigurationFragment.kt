package com.example.pvtcalculation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.pvtcalculation.databinding.ConfigurationFragmentBinding

class ConfigurationFragment : Fragment() {

    companion object {
        fun newInstance() = ConfigurationFragment()
    }

    private lateinit var binding: ConfigurationFragmentBinding
    private lateinit var viewModel: ConfigurationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.configuration_fragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfigurationViewModel::class.java)

        binding.configurationViewModel = viewModel

        viewModel.buttonLoad.observe(viewLifecycleOwner, Observer{button ->
            if (button){
                viewModel.onButtonLoadComplete()
                Navigation.findNavController(requireView()).navigate(ConfigurationFragmentDirections.actionConfigurationFragmentToConfigurationLoadFragment())
            }

        })

        viewModel.buttonDownload.observe(viewLifecycleOwner, Observer{button ->
            if (button){
                viewModel.onButtonDownloadComplete()
                Navigation.findNavController(requireView()).navigate(ConfigurationFragmentDirections.actionConfigurationFragmentToListPositionsFragment())
            }

        })
    }

}