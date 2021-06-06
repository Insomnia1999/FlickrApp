package com.test.flickrapp.ui

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.flickrapp.data.PhotoDetail
import com.test.flickrapp.data.Photos
import com.test.flickrapp.data.PhotosRepositoryImpl
import com.test.flickrapp.extensions.BadResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosViewModel(
    private val photosRepository: PhotosRepositoryImpl,
    private val app: Application
) : AndroidViewModel(app) {

    var isLastPage = false
    var totalPageCount = 0

    private val mutableArePhotosRequested = MutableLiveData<Boolean>().apply { value = false }
    val arePhotosRequested: LiveData<Boolean> = mutableArePhotosRequested

    private val mutableRequestPhotosError = MutableLiveData<Boolean>().apply { value = false }
    val requestPhotosError: LiveData<Boolean> = mutableRequestPhotosError

    private val mutablePhotosData = MutableLiveData<Photos>().apply { value = null }
    val photosData: LiveData<Photos> = mutablePhotosData

    private val mutableIsPhotoDetailRequested = MutableLiveData<Boolean>().apply { value = false }
    val isPhotoDetailRequested: LiveData<Boolean> = mutableIsPhotoDetailRequested

    private val mutableRequestPhotoDetailError = MutableLiveData<Boolean>().apply { value = false }
    val requestPhotoDetailError: LiveData<Boolean> = mutableRequestPhotoDetailError

    private val mutablePhotoDetailData = MutableLiveData<PhotoDetail>().apply { value = null }
    val photoDetailData: LiveData<PhotoDetail> = mutablePhotoDetailData


    fun getPhotos(searchTerm: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableRequestPhotosError.postValue(false)

            try {
                val responseData = photosRepository.getImages(searchTerm, page)

                responseData.photos?.photo?.let { photos ->

                    val test = photos
                    if(photos.isNotEmpty()) {
                        photos.map { photo ->
                            photo.url =
                                "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg,"
                        }
                    }
                }


                mutablePhotosData.postValue(responseData)
                mutableArePhotosRequested.postValue(true)

            } catch (e: BadResponseException) {
                mutableArePhotosRequested.postValue(false)
                mutableRequestPhotosError.postValue(true)

            } catch (e: NetworkErrorException) {
                mutableArePhotosRequested.postValue(false)
                mutableRequestPhotosError.postValue(true)
            }

        }
    }

    fun getPhotoDetail(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableRequestPhotoDetailError.postValue(false)

            try {
                val responseData = photosRepository.getPhotoDetail(id)
                mutablePhotoDetailData.postValue(responseData)
                mutableIsPhotoDetailRequested.postValue(true)

            } catch (e: BadResponseException) {
                mutableIsPhotoDetailRequested.postValue(false)
                mutableRequestPhotoDetailError.postValue(true)

            } catch (e: NetworkErrorException) {
                mutableIsPhotoDetailRequested.postValue(false)
                mutableRequestPhotoDetailError.postValue(true)
            }

        }
    }
}