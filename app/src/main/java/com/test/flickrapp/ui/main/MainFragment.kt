package com.test.flickrapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.flickrapp.R
import com.test.flickrapp.data.PhotoResponse
import com.test.flickrapp.data.State
import com.test.flickrapp.ui.PhotosViewModel
import com.test.flickrapp.ui.main.adapter.PhotosAdapter
import com.test.flickrapp.ui.main.adapter.PhotosClickListener
import com.test.flickrapp.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment: Fragment(), PhotosClickListener {

    private val photosViewModel: PhotosViewModel by viewModel()

    private val photos: MutableList<PhotoResponse> = mutableListOf()

    private val photosAdapter: PhotosAdapter? by lazy {
        PhotosAdapter(this, requireActivity())
    }

    private var searchJob: Job? = null

    private var currentPage = 1
    private var isPhotosLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSwipePhotos()

        setPhotosList()

        setPhotosObserver()

        setPhotosErrorObserver()

        setSearchBoxChangeListener()

        setRemoveButton()
    }

    //Observers
    private fun setPhotosObserver() {
        photosViewModel.photosData.observe(viewLifecycleOwner, { photosResponse ->

            photosResponse?.let { photos ->
                photos.photos?.photo?.let { photoList ->
                    if (photoList.isNotEmpty()) {
                        photoRecycler?.visibility = View.VISIBLE
                        emptyLayout.visibility = View.GONE

                        this.photos.addAll(photos.photos.photo)
                        photosAdapter?.displayPhotos(this.photos)

                    } else {
                        photoRecycler?.visibility = View.GONE
                        emptyLayout.visibility = View.VISIBLE
                    }

                } ?: run {
                    photoRecycler?.visibility = View.GONE
                    emptyLayout.visibility = View.VISIBLE
                }
                swipePhotos?.isRefreshing = false
                isPhotosLoading = false
            }

        })

    }

    private fun setPhotosErrorObserver() {
        photosViewModel.requestPhotosError.observe(viewLifecycleOwner, { photosError ->
            if (photosError) {
                photoRecycler?.visibility = View.GONE
                emptyLayout?.visibility = View.VISIBLE

                swipePhotos.isRefreshing = false

                photosAdapter?.setState(State.ERROR)
            }
        })
    }

    //Listeners
    override fun onPhotoClick(photoResponse: PhotoResponse) {
        findNavController().navigate(MainFragmentDirections.goDetail(photoResponse.id, photoResponse.url))
    }

    private fun setRemoveButton() {
        removeButton?.setOnClickListener {
            searchBox.setText("")
        }
    }

    private fun setSearchBoxChangeListener() {
        searchBox.addTextChangedListener {
            swipePhotos.isRefreshing = true
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(1000)
                photos.clear()
                photosAdapter?.clear()
                currentPage = 1
                photosViewModel.getPhotos(it.toString(), currentPage)
            }

        }
    }
    //Methods

    private fun setPhotosList() {
        val layoutManager = GridLayoutManager(requireActivity(), 2)

        photoRecycler?.apply {
            this.layoutManager = layoutManager
            adapter = photosAdapter
        }

        photoRecycler?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                photosAdapter?.setState(State.LOADING)

                currentPage++

                photosViewModel.getPhotos(searchBox.text.toString(), currentPage)
            }

            override fun lastPage() {
                photosAdapter?.setState(State.DONE)
            }

            override val totalPageCount: Int
                get() = photosViewModel?.totalPageCount!!
            override val isLastPage: Boolean
                get() = photosViewModel?.isLastPage!!
            override val isLoading: Boolean
                get() = isPhotosLoading

        })

        photoRecycler?.post { layoutManager.setOrientation(LinearLayoutManager.VERTICAL) }
    }

    private fun setSwipePhotos() {
        swipePhotos?.setOnRefreshListener {
            reloadPhotos()
        }
    }

    private fun reloadPhotos() {
        isPhotosLoading = true
        swipePhotos?.isRefreshing = true
        photos.clear()
        photosAdapter?.clear()
        currentPage = 1
        photosViewModel.getPhotos(searchBox.text.toString(), currentPage)
    }
}