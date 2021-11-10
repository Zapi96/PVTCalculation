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
import com.example.pvtcalculation.databinding.WelcomeFragmentBinding

class WelcomeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    private lateinit var binding: WelcomeFragmentBinding
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.welcome_fragment,
            container,
            false
        )



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)

        binding.welcomeViewModel = viewModel

        viewModel.buttonConfiguration.observe(viewLifecycleOwner, Observer{button ->
            if (button){
                viewModel.onButtonConfigurationComplete()
                Navigation.findNavController(requireView()).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToConfigurationFragment())
            }

        })

    }

}