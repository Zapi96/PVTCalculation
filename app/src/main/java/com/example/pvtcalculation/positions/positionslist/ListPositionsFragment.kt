package com.example.pvtcalculation.positions.positionslist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.pvtcalculation.R
import com.example.pvtcalculation.base.BaseFragment
import com.example.pvtcalculation.base.NavigationCommand
import com.example.pvtcalculation.databinding.ListPositionsFragmentBinding
import com.example.pvtcalculation.utils.setup
import org.koin.androidx.viewmodel.ext.android.viewModel



class ListPositionsFragment : BaseFragment() {
    //use Koin to retrieve the ViewModel instance
    private lateinit var binding: ListPositionsFragmentBinding
//    private val _viewModel: ListPositionsViewModel by lazy {
//        ViewModelProvider(this).get(ListPositionsViewModel::class.java)
//    }

    override val _viewModel: ListPositionsViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.list_positions_fragment, container, false
            )
        binding.viewModel = _viewModel

        setHasOptionsMenu(true)

        binding.refreshLayout.setOnRefreshListener { _viewModel.loadLocations() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
        binding.addReminderFAB.setOnClickListener {
            navigateToAddReminder()
        }
    }

    override fun onResume() {
        super.onResume()
        //load the reminders list on the ui
        _viewModel.loadLocations()
    }

    private fun navigateToAddReminder() {
        //use the navigationCommand live data to navigate between the fragments
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                ListPositionsFragmentDirections.actionListPositionsFragmentToConfigurationDownloadFragment()
            )
        )
    }

    private fun setupRecyclerView() {
        val adapter = ListPositionsAdapter {
        }

//        setup the recycler view using the extension function
        binding.reminderssRecyclerView.setup(adapter)
    }




}

