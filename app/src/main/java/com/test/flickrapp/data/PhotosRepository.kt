package com.test.flickrapp.data

import android.accounts.NetworkErrorException
import com.test.flickrapp.extensions.BadResponseException

interface PhotosRepository {
    @Throws(NetworkErrorException::class, BadResponseException::class)
    suspend fun getImages(searchTerm: String, page: Int): Photos

    @Throws(NetworkErrorException::class, BadResponseException::class)
    suspend fun getPhotoDetail(id: String): PhotoDetail
}