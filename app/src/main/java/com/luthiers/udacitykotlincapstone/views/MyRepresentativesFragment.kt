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
import com.luthiers.udacitykotlincapstone.viewModels.MyRepresentativesViewModel
import com.luthiers.udacitykotlincapstone.views.adapters.MyRepresentativesAdapter


class MyRepresentativesFragment : Fragment() {

    private val viewModel by viewModels<MyRepresentativesViewModel>()

    private val PERMISSION_ID = 7

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var _myRepresentativesAdapter: MyRepresentativesAdapter

    private lateinit var binding: FragmentMyRepresentativesBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyRepresentativesBinding.inflate(inflater, container, false)

        _myRepresentativesAdapter = MyRepresentativesAdapter(viewModel)

        with(binding.rvMyRepresentatives) {
            setHasFixedSize(true)
            adapter = _myRepresentativesAdapter
        }

        with(binding) {
            btnCurrentLocation.setOnClickListener {
                viewModel.getLastLocation(
                    requireActivity(),
                    fusedLocationClient,
                    PERMISSION_ID
                )
            }

            btnUseLocation.setOnClickListener { viewModel.setQueryLocation(etLocation.text.toString()) }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            error.observe(viewLifecycleOwner, Observer { errorString ->
                Toast.makeText(requireContext(), errorString, Toast.LENGTH_LONG).show()
            })

            representatives.observe(viewLifecycleOwner, Observer { representatives ->
                _myRepresentativesAdapter.addRepresentatives(representatives)
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
