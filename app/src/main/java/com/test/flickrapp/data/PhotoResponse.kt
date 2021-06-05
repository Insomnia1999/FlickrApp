package com.test.flickrapp.data

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    @SerializedName("ownername") val ownerName: String,
    var url: String
)