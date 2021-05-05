package com.example.foursquareapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    private val status: Int,
    private val message: String,
    private val pageNo: Int,
    private val pageSize: Int,
    private val lastPage: Boolean,
    private val data: ArrayList<DataPlace>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        arrayListOf<DataPlace>().apply {
            parcel.readTypedList(this, DataPlace)
        }
    ) {
    }

    fun getStatus() = status
    fun getMessage() = message
    fun getPageNo() = pageNo
    fun getPageSize() = pageSize
    fun getLastPage() = lastPage
    fun getData() = data

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status)
        parcel.writeString(message)
        parcel.writeInt(pageNo)
        parcel.writeInt(pageSize)
        parcel.writeByte(if (lastPage) 1 else 0)
        parcel.writeTypedList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceResponse> {
        override fun createFromParcel(parcel: Parcel): PlaceResponse {
            return PlaceResponse(parcel)
        }

        override fun newArray(size: Int): Array<PlaceResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class DataPlace(
    private val place: Place?,
    private val distance: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Place::class.java.classLoader),
        parcel.readDouble()
    ) {


    }
    fun getPlace() = place
    fun getDistance() = distance

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(place, flags)
        parcel.writeDouble(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataPlace> {
        override fun createFromParcel(parcel: Parcel): DataPlace {
            return DataPlace(parcel)
        }

        override fun newArray(size: Int): Array<DataPlace?> {
            return arrayOfNulls(size)
        }
    }

}



