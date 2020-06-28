package com.luthiers.udacitykotlincapstone.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.luthiers.udacitykotlincapstone.data.local.SingleElectionRoomDatabase
import com.luthiers.udacitykotlincapstone.data.models.SingleElection
import com.luthiers.udacitykotlincapstone.data.network.INetworkDataSource
import com.luthiers.udacitykotlincapstone.data.network.NetworkDataSource
import com.luthiers.udacitykotlincapstone.data.network.retrofit
import com.luthiers.udacitykotlincapstone.data.repositiories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class UpcomingElectionsViewModel(_application: Application): AndroidViewModel(_application) {

    private val mainRepository =
        MainRepository(
            NetworkDataSource(retrofit.create(INetworkDataSource::class.java)),
            SingleElectionRoomDatabase.getDatabase(_application).singleElectionDao()
        )

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val elections = liveData(Dispatchers.IO) {
        _isLoading.postValue(true)

        val networkResult = mainRepository.getElectionsFromNetwork()
        val arrayOfElections = networkResult.elections.toTypedArray()
        mainRepository.insertElections(*arrayOfElections)

        _isLoading.postValue(false)

        emitSource(mainRepository.getElectionsFromLocalStorage())
    }

    @Suppress("USELESS_CAST")
    val savedElections =
        (mainRepository.getSavedElections() as Flow<List<SingleElection>>).asLiveData()
}