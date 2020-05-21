package com.luthiers.udacitykotlincapstone.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "elections")
data class SingleElection(
    @PrimaryKey
    @field:Json(name = "id") var id: String,

    @ColumnInfo(name = "name")
    @field:Json(name = "name") var name: String,

    @ColumnInfo(name = "election_day")
    @field:Json(name = "electionDay") var electionDay: String,

    @ColumnInfo(name = "ocd_division_id")
    @field:Json(name = "ocdDivisionId") var divisionID: String,

    var isSaved: Int = 0 // Where 0 indicates that the single election is not saved
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(electionDay)
        parcel.writeString(divisionID)
        parcel.writeInt(isSaved)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SingleElection> {
        override fun createFromParcel(parcel: Parcel): SingleElection {
            return SingleElection(parcel)
        }

        override fun newArray(size: Int): Array<SingleElection?> {
            return arrayOfNulls(size)
        }
    }
}