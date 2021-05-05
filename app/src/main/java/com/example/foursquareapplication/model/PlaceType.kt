package com.example.foursquareapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PlaceType(
    @SerializedName("id")
    private val categoryId: Int,
    @SerializedName("name")
    private val categoryName: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    fun getCategoryId() = categoryId
    fun getCategoryName() = categoryName
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(categoryId)
        parcel.writeString(categoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceType> {
        override fun createFromParcel(parcel: Parcel): PlaceType {
            return PlaceType(parcel)
        }

        override fun newArray(size: Int): Array<PlaceType?> {
            return arrayOfNulls(size)
        }
    }

}
