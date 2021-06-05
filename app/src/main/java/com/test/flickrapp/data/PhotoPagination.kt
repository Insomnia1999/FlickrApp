package com.test.flickrapp.data

data class PhotoPagination (
    val page: Int,
    val photo: List<PhotoResponse>?
)