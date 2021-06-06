package com.test.flickrapp.data

import android.accounts.NetworkErrorException
import com.test.flickrapp.extensions.BadResponseException
import com.test.flickrapp.extensions.executeHandlingNetworkError


class PhotosRepositoryImpl(private val flickrApi: FlickrApi): PhotosRepository {

    @Throws(NetworkErrorException::class, BadResponseException::class)
    override suspend fun getImages(searchTerm: String, page: Int): Photos {
        return flickrApi.getPhotos(searchTerm, page).executeHandlingNetworkError()
    }

    @Throws(NetworkErrorException::class, BadResponseException::class)
    override suspend fun getPhotoDetail(id: String): PhotoDetail {
        return flickrApi.getPhotoDetail(id).executeHandlingNetworkError()
    }
}