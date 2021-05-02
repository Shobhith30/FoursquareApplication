package com.example.foursquareapplication.model

import android.graphics.Bitmap
import java.io.Serializable

class PhotoModel(image: Int) :Serializable {

    var image:Int?= image

}

class Model(image: Bitmap) :Serializable{

    var image: Bitmap?= image

}
