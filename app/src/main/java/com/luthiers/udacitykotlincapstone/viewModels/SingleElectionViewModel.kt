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
import kotlinx.coroutines.launch

class SingleElectionViewModel(private val _application: Application): AndroidViewModel(_application) {

    private val _electionID = MutableLiveData<String>()

    private val mainRepository =
        MainRepository(
            NetworkDataSource(retrofit.create(INetworkDataSource::class.java)),
            SingleElectionRoomDatabase.getDatabase(_application).singleElectionDao()
        )

    val election = _electionID.switchMap {
        liveData { emitSource(mainRepository.getElection(it)) }
    }

    fun toggleElectionAsSaved(singleElection: SingleElection) {
        viewModelScope.launch(Dispatchers.IO) {
            singleElection.isSaved = if (singleElection.isSaved == 0) 1 else 0

            mainRepository.insertElection(singleElection)
        }
    }

    fun setElectionID(id: String) {
        _electionID.value = id
    }
}