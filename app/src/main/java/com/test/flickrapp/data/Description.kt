package com.test.flickrapp.data

import com.google.gson.annotations.SerializedName

data class Description (
    @SerializedName("_content") val content: String
        )