package com.luthiers.udacitykotlincapstone.viewModels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.luthiers.udacitykotlincapstone.R
import com.luthiers.udacitykotlincapstone.data.local.SingleElectionRoomDatabase
import com.luthiers.udacitykotlincapstone.data.models.SingleRepresentative
import com.luthiers.udacitykotlincapstone.data.network.INetworkDataSource
import com.luthiers.udacitykotlincapstone.data.network.NetworkDataSource
import com.luthiers.udacitykotlincapstone.data.network.retrofit
import com.luthiers.udacitykotlincapstone.data.repositiories.MainRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.util.*

class MyRepresentativesViewModel(private val _application: Application): AndroidViewModel(_application) {

    private val _location = MutableLiveData<String>()

    private val mainRepository =
        MainRepository(
            NetworkDataSource(retrofit.create(INetworkDataSource::class.java)),
            SingleElectionRoomDatabase.getDatabase(_application).singleElectionDao()
        )

    private val _timer = Timer()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val representatives: LiveData<List<SingleRepresentative>> = _location.switchMap {
        liveData(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val result = mainRepository.getOfficialsAsync(it)

                emit(result.officials)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    val errorMessage = _application.baseContext.getString(R.string.no_results_found)
                    _error.postValue(errorMessage)
                }
            } finally {
                _isLoading.postValue(false)
            }
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

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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

    fun setQueryLocation(location: String) {
        // Add a delay for the user location parameter
        _timer.schedule(object: TimerTask() {
            override fun run() {
                _location.postValue(location)
            }
        }, 350) // Add a delay of 350ms
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



    @SuppressLint("MissingPermission")
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
                            address?.let { _location.value = it.locality }
                        } else _error.value =
                            _application.baseContext.getString(R.string.error_getting_current_location)

                    } else _error.value = task.exception?.localizedMessage
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivity(intent)
            }
        } else {
            requestPermissions(activity, PERMISSION_ID)
        }
    }
}