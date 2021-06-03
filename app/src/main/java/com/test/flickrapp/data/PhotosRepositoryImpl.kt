package com.test.flickrapp.data

import android.accounts.NetworkErrorException
import com.test.flickrapp.extensions.BadResponseException
import com.test.flickrapp.extensions.executeHandlingNetworkError


class PhotosRepositoryImpl(private val flickrApi: FlickrApi): PhotosRepository {

    @Throws(NetworkErrorException::class, BadResponseException::class)
    override suspend fun getImages(searchTerm: String): Photos {
        return flickrApi.getPhotos(searchTerm).executeHandlingNetworkError()
    }
}