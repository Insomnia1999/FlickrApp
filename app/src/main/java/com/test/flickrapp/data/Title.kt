package com.test.flickrapp.data

import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("_content") val content: String
    )