package com.example.foursquareapplication

import android.graphics.Bitmap
import java.io.Serializable

class PhotoModel:Serializable {

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
