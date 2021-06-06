package com.test.flickrapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.test.flickrapp.R
import com.test.flickrapp.data.PhotoDetail
import com.test.flickrapp.databinding.FragmentPhotoDetailBinding
import com.test.flickrapp.ui.PhotosViewModel
import kotlinx.android.synthetic.main.fragment_photo_detail.*
import kotlinx.android.synthetic.main.list_item_photo.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoDetailFragment: Fragment() {

    private val photosViewModel: PhotosViewModel by viewModel()

    private val args: PhotoDetailFragmentArgs by navArgs()

    private lateinit var dataBinding: FragmentPhotoDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate<FragmentPhotoDetailBinding>(
            inflater, R.layout.fragment_photo_detail, container, false
        )

        dataBinding.lifecycleOwner = this

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackButton()

        setPhotoDetailDataObserver()

        setPhotoErrorObserver()

        setPhotoClickListener()

        Picasso.get().load(args.url).into(photoImageView)

        photosViewModel.getPhotoDetail(args.id)
    }

    //Observers
    private fun setPhotoDetailDataObserver() {
        photosViewModel.isPhotoDetailRequested.observe(viewLifecycleOwner, { isPhotoDetailRequested ->
            if(isPhotoDetailRequested) {
                photosViewModel.photoDetailData.value?.let {
                    this.dataBinding.photoDetail = it
                    this.dataBinding.executePendingBindings()
                }
            }
        })
    }

    private fun setPhotoErrorObserver() {
        photosViewModel.requestPhotoDetailError.observe(viewLifecycleOwner, {
            if(it) {
                Toast.makeText(requireActivity(), "Ha ocurrido un error al recuperar la imagen", Toast.LENGTH_LONG).show()
            }
        })
    }


    //Listeners
    private fun setBackButton() {
        backButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setPhotoClickListener() {
        photoImageView?.setOnClickListener {
            findNavController().navigate(PhotoDetailFragmentDirections.goPhotoViewer(args.url))
        }
    }

    //Methods
}