package com.example.foursquareapplication

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Photo:Serializable {

    var image:Int?=null

    constructor(image:Int){
        this.image=image
    }
}

class Model :Serializable{

    var image: Bitmap?=null

    constructor(image: Bitmap){
        this.image=image
    }
}
