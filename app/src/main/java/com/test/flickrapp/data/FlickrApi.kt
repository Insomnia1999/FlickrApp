package com.test.flickrapp.data

import com.test.flickrapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=${
        Constants.flickrKey}")
    suspend fun getPhotos(@Query("text") searchTerm: String): Call<Photos>
}