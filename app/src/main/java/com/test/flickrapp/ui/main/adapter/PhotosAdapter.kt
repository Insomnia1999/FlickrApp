package com.test.flickrapp.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.flickrapp.R
import com.test.flickrapp.data.PhotoResponse
import com.test.flickrapp.data.State
import kotlinx.android.synthetic.main.list_item_loading.view.*
import kotlinx.android.synthetic.main.list_item_photo.view.*
import com.test.flickrapp.BR

class PhotosAdapter(val listener: PhotosClickListener?, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DATA_VIEW_TYPE = 1
    private val LOADING_VIEW_TYPE = 2

    var photos: MutableList<PhotoResponse>? = null
    private var state = State.LOADING

    fun refresh() {
        this.notifyDataSetChanged()

    }

    fun clear() {
        photos?.clear()
        this.notifyDataSetChanged()
    }

    fun displayPhotos(photos: List<PhotoResponse>) {
        if (this.photos == null) this.photos =
            emptyList<PhotoResponse>().toMutableList()

        this.photos?.clear()
        this.photos?.addAll(photos)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == DATA_VIEW_TYPE) {
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                inflater,
                R.layout.list_item_photo,
                parent,
                false
            )
            PhotoViewHolder(binding)
        } else {
            val view = inflater.inflate(R.layout.list_item_loading, parent, false)
            LoadViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (photos == null) {
            return LOADING_VIEW_TYPE
        }

        return if (position < photos!!.size) DATA_VIEW_TYPE else LOADING_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            photos?.let {
                val product = it[position]
                val productViewHolder = holder as PhotoViewHolder
                productViewHolder.bind(product, listener)
            }
        } else (holder as LoadViewHolder).bind(state)
    }

    override fun getItemCount(): Int {
        if (photos == null) {
            return 0
        }

        return photos!!.size + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        if (photos == null) {
            return false
        }

        return photos!!.size != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        if (photos != null) {
            this.state = state
            notifyItemChanged(photos?.size!!)
        }
    }

}

class PhotoViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(photoResponse: PhotoResponse, listener: PhotosClickListener?) {
        binding.setVariable(BR.photo, photoResponse)
        binding.executePendingBindings()

        Picasso.get().load(photoResponse.url).into(itemView.photoImageView)
        setClickListener(photoResponse, listener)
    }

    private fun setClickListener(photoResponse: PhotoResponse, callback: PhotosClickListener?) {
        itemView.setOnClickListener { callback?.onPhotoClick(photoResponse) }
    }
}

interface PhotosClickListener {

    fun onPhotoClick(photoResponse: PhotoResponse)
}

class LoadViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: State?) {
        itemView.progress_bar.visibility =
            if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
    }
}