package com.luthiers.udacitykotlincapstone.data.repositiories

import android.location.Address
import com.luthiers.udacitykotlincapstone.data.local.SingleElectionDao
import com.luthiers.udacitykotlincapstone.data.local.SingleElectionRoomDatabase
import com.luthiers.udacitykotlincapstone.data.models.SingleElection
import com.luthiers.udacitykotlincapstone.data.network.NetworkDataSource

class MainRepository(
    private val networkDataSource: NetworkDataSource,
    private val savedElectionsDao: SingleElectionDao
) {
    suspend fun getElectionsFromNetwork() =
        networkDataSource.getElections()

    fun getElectionsFromLocalStorage() =
        savedElectionsDao.getElections()

    fun getElection(id: String) =
        savedElectionsDao.getElection(id)

    fun getSavedElections() =
        savedElectionsDao.getSavedElections()

    suspend fun getOfficialsAsync(location: String) =
        networkDataSource.getOfficialsAsync(location)

    /**
     * Function that saved a [SingleElection] as a saved election inside the
     * [SingleElectionRoomDatabase]
     * */
    fun insertElection(singleElection: SingleElection) =
        savedElectionsDao.insertElection(singleElection)

    fun insertElections(vararg elections: SingleElection) =
        savedElectionsDao.insertElections(*elections)
}