package com.example.pvtcalculation

import android.Manifest
import android.bluetooth.BluetoothGattCharacteristic.PERMISSION_WRITE
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pvtcalculation.databinding.ConfigurationFragmentBinding
import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL


class ConfigurationFragment : Fragment() {

    companion object {
        fun newInstance() = ConfigurationFragment()
    }

    private lateinit var binding: ConfigurationFragmentBinding
    private lateinit var viewModel: ConfigurationViewModel
    private lateinit var imageview: ImageView

    private val url = "https://www.pngall.com/wp-content/uploads/2016/04/Satellite-PNG-File.png"

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

        binding.lifecycleOwner = this

        imageview = binding.imageSatellite

        checkPermission()
        val uri = url.toUri().buildUpon().scheme("https").build()
        //TODO: Add Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
        Glide.with(imageview)
            .load(uri)
            .apply(
                RequestOptions().override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .placeholder(R.drawable.ic_no_data)
                    .error(R.drawable.ic_no_data))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageview)
//        Glide.with(this)
//            .load(url)
//            .apply(RequestOptions.circleCropTransform())
//            .into(imageview);

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

    override fun onResume() {
        super.onResume()
        imageview = binding.imageSatellite

        val uri = url.toUri().buildUpon().scheme("https").build()
        //TODO: Add Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
        Glide.with(imageview)
            .load(uri)
            .apply(
                RequestOptions().override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .placeholder(R.drawable.ic_no_data)
                    .error(R.drawable.ic_no_data))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageview)

//        Glide.with(this)
//            .load(url)
//            .apply(RequestOptions.circleCropTransform())
//            .into(imageview);
        //set again your ImageView source here..
    }

    fun checkPermission(): Boolean {
        val READ_EXTERNAL_PERMISSION =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        if (READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_WRITE
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == PERMISSION_WRITE && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }

}