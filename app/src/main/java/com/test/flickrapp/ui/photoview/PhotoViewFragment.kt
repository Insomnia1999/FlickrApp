package com.test.flickrapp.ui.photoview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.test.flickrapp.R
import com.test.flickrapp.ui.detail.PhotoDetailFragmentArgs
import kotlinx.android.synthetic.main.fragment_photo_view.*

class PhotoViewFragment: Fragment() {

    private val args: PhotoViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackButton()

        Picasso.get().load(args.url).into(photoView)
    }

    private fun setBackButton() {
        backButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}