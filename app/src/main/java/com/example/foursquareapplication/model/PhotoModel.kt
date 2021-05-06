package com.example.foursquareapplication.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class PhotoModel(image: Int) :Serializable {

    var image:Int?= image

}

class Model(image: Bitmap) :Serializable{

    var image: Bitmap?= image

}

class ReviewPhotos(image:Uri){
    var image:Uri?=image
}

/*data class Model1(val bf:Int?=0) : Parcelable {

    var image: Bitmap?=null

    constructor(parcel: Parcel) : this() {
        image = parcel.readParcelable(Bitmap::class.java.classLoader)
    }

    constructor(image: Bitmap) : this() {
        this.image=image
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(image, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Model1> {
        override fun createFromParcel(parcel: Parcel): Model1 {
            return Model1(parcel)
        }

        override fun newArray(size: Int): Array<Model1?> {
            return arrayOfNulls(size)
        }
    }


}*/
