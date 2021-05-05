package com.example.foursquareapplication.model
import android.os.Parcel
import android.os.Parcelable
import com.example.foursquareapplication.model.PlaceType
import com.google.gson.annotations.SerializedName

data class Place(

    @SerializedName("id")
    private val placeId: Int,
    private val name: String,
    private val placeType: ArrayList<PlaceType>,
    private val overallRating: Float,
    private val latitude: Double,
    private val longitude: Double,
    private val cost: Int,
    private val phone: Long,
    private val landmark: String,
    private val address: String,
    private val overview: String,
    private val image: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        arrayListOf<PlaceType>().apply {
            parcel.readTypedList(this, PlaceType)
        },
        parcel.readFloat(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    fun getPlaceId() = placeId
    fun getName() = name
    fun getPlaceType() = placeType
    fun getOverallRating() = overallRating
    fun getLatitude() = latitude
    fun getLongitude() = longitude
    fun getCost() = cost
    fun getPhone() = phone
    fun getLandmark() = landmark
    fun getAddress() = address
    fun getOverview() = overview
    fun getImage() = image
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(placeId)
        parcel.writeString(name)
        parcel.writeTypedList(placeType)
        parcel.writeFloat(overallRating)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeInt(cost)
        parcel.writeLong(phone)
        parcel.writeString(landmark)
        parcel.writeString(address)
        parcel.writeString(overview)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Place> {
        override fun createFromParcel(parcel: Parcel): Place {
            return Place(parcel)
        }

        override fun newArray(size: Int): Array<Place?> {
            return arrayOfNulls(size)
        }
    }

}
