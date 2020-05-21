package com.luthiers.udacitykotlincapstone.views

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.luthiers.udacitykotlincapstone.databinding.FragmentMyRepresentativesBinding
import com.luthiers.udacitykotlincapstone.viewModels.MainViewModel
import com.luthiers.udacitykotlincapstone.views.adapters.MyRepresentativesAdapter


class MyRepresentativesFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel>()

    private val PERMISSION_ID = 7

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyRepresentativesBinding.inflate(inflater, container, false)

        val representativesAdapter = MyRepresentativesAdapter()

        with(binding.rvMyRepresentatives) {
            setHasFixedSize(true)
            adapter = representativesAdapter
        }

        viewModel.representatives.observe(viewLifecycleOwner, Observer { representatives ->
//           representativesAdapter.addRepresentatives(representatives)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorString ->
            Toast.makeText(requireContext(), errorString, Toast.LENGTH_LONG).show()
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel.getLastLocation(requireActivity(), fusedLocationClient, PERMISSION_ID)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The current user granted the location permission
                viewModel.getLastLocation(requireActivity(), fusedLocationClient, PERMISSION_ID)
            }
        }
    }
}
