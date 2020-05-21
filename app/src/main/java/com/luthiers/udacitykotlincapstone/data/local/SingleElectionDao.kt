package com.luthiers.udacitykotlincapstone.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.luthiers.udacitykotlincapstone.data.models.SingleElection
import kotlinx.coroutines.flow.Flow

@Dao
interface SingleElectionDao {
    @Query("SELECT * FROM elections WHERE isSaved == 1")
    fun getSavedElections(): Flow<List<SingleElection>>

    @Query("SELECT * FROM elections")
    fun getElections(): LiveData<List<SingleElection>>

    @Query("SELECT * FROM elections WHERE id=:id")
    fun getElection(id: String):  LiveData<SingleElection>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(singleElection: SingleElection): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertElections(vararg elections: SingleElection)
}