package com.test.flickrapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.test.flickrapp.data.PhotosRepositoryImpl

class PhotosViewModel(
    private val photosRepository: PhotosRepositoryImpl,
    private val app: Application
) : AndroidViewModel(app) {

}