package com.luthiers.udacitykotlincapstone.viewModels

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.luthiers.udacitykotlincapstone.R
import com.luthiers.udacitykotlincapstone.data.local.SingleElectionRoomDatabase
import com.luthiers.udacitykotlincapstone.data.models.SingleElection
import com.luthiers.udacitykotlincapstone.data.models.SingleRepresentative
import com.luthiers.udacitykotlincapstone.data.network.INetworkDataSource
import com.luthiers.udacitykotlincapstone.data.network.NetworkDataSource
import com.luthiers.udacitykotlincapstone.data.network.retrofit
import com.luthiers.udacitykotlincapstone.data.repositiories.MainRepository
import com.luthiers.udacitykotlincapstone.views.UpcomingElectionsFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mainRepository =
        MainRepository(
            NetworkDataSource(retrofit.create(INetworkDataSource::class.java)),
            SingleElectionRoomDatabase.getDatabase(application).singleElectionDao()
        )

    val elections = liveData(Dispatchers.IO) {
        _isLoading.postValue(true)

        val networkResult = mainRepository.getElectionsFromNetwork()
        val arrayOfElections = networkResult.elections.toTypedArray()
        mainRepository.insertElections(*arrayOfElections)

        _isLoading.postValue(false)

        emitSource(mainRepository.getElectionsFromLocalStorage())
    }

    private val _electionID = MutableLiveData<String>()

    val election = _electionID.switchMap {
        liveData { emitSource(mainRepository.getElection(it)) }
    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _representatives = MutableLiveData<List<SingleRepresentative>>()
    val representatives: LiveData<List<SingleRepresentative>> = _representatives

    @Suppress("USELESS_CAST")
    val savedElections =
        (mainRepository.getSavedElections() as Flow<List<SingleElection>>).asLiveData()

    fun openSingleElection(view: View, singleElection: SingleElection) {
        val directions =
            UpcomingElectionsFragmentDirections.actionUpcomingElectionsFragmentToSingleElectionFragment(
                singleElection
            )

        // Pass the single election into the SingleElectionFragment
        view.findNavController().navigate(directions)
    }

    fun setElectionID(id: String) {
        _electionID.value = id
    }

    fun toggleElectionAsSaved(singleElection: SingleElection) {
        viewModelScope.launch(Dispatchers.IO) {
            singleElection.isSaved = if (singleElection.isSaved == 0) 1 else 0

            mainRepository.insertElection(singleElection)
        }
    }

    fun openMyRepresentatives(view: View) {
        // Take the user to the upcoming elections fragment
        view.findNavController().navigate(R.id.action_mainFragment_to_myRepresentativesFragment)
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermissions(activity: Activity, PERMISSION_ID: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    fun getLastLocation(
        activity: Activity,
        fusedLocationProviderClient: FusedLocationProviderClient,
        PERMISSION_ID: Int
    ) {
        if (checkPermissions(activity)) {
            if (isLocationEnabled(activity)) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location != null) {
                        if (Geocoder.isPresent()) {
                            val address = getCurrentUserAddress(
                                activity,
                                location.latitude,
                                location.longitude
                            )
                            address?.let { getRepresentatives(it) }
                        } else {
                            _error.value = "There was a error getting your current location"
                        }
                        // Fetch the representatives
                        Log.d(
                            "Representatives",
                            "The location lat is ${location.latitude} and the location longitude is ${location.longitude}"
                        )
                    } else {
                        _error.value = task.exception?.localizedMessage
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivity(intent)
            }
        } else {
            requestPermissions(activity, PERMISSION_ID)
        }
    }

    private fun getCurrentUserAddress(
        context: Context,
        latitude: Double,
        longitude: Double
    ): Address? {
        val geoCoder = Geocoder(context, Locale.getDefault())

        val address = geoCoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Set max results to be 1 since we only want one result

        return if (address.isNotEmpty()) address[0] else null
    }

    private fun getRepresentatives(address: Address) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val result = mainRepository.getOfficialsAsync(address)

            _isLoading.postValue(false)

            _representatives.postValue(result.officials)
        }
    }

    private fun checkPermissions(context: Context) =
        (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                )

    fun openUpcomingElections(view: View) {
        // Take the user to the UpcomingElections fragment
        view.findNavController().navigate(R.id.action_mainFragment_to_upcomingElectionsFragment)
    }
}