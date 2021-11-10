package com.example.pvtcalculation.base

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.pvtcalculation.R
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.Navigation


abstract class BaseFragment : Fragment() {

    abstract val _viewModel: BaseViewModel

    override fun onStart() {
        super.onStart()
        _viewModel.showErrorMessage.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
        _viewModel.showToast.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
        _viewModel.showSnackBar.observe(this, Observer {
            Snackbar.make(this.view!!, it, Snackbar.LENGTH_LONG).show()
        })
        _viewModel.showSnackBarInt.observe(this, Observer {
            Snackbar.make(this.view!!, getString(it), Snackbar.LENGTH_LONG).show()
        })

        _viewModel.navigationCommand.observe(this, Observer { command ->
            when (command) {
                is NavigationCommand.To -> Navigation.findNavController(requireView()).navigate(command.directions)
                is NavigationCommand.Back -> Navigation.findNavController(requireView()).popBackStack()
                is NavigationCommand.BackTo -> Navigation.findNavController(requireView()).popBackStack(
                    command.destinationId,
                    false
                )
            }
        })
    }
}