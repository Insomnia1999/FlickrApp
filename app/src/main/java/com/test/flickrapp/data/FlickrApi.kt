package com.test.flickrapp.data

import com.test.flickrapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&extras=owner_name&per_page=10&api_key=${Constants.flickrKey}")
    fun getPhotos(@Query("text") searchTerm: String, @Query("page") page: Int): Call<Photos>

    @GET("?method=flickr.photos.getInfo&nojsoncallback=1&format=json&api_key=${Constants.flickrKey}")
    fun getPhotoDetail(@Query("photo_id") id: String): Call<PhotoDetail>
}