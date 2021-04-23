package com.example.foursquareapplication

import java.io.Serializable

class PhotoModel:Serializable {

    var image:Int?=null

    constructor(image:Int){
        this.image=image
    }
}