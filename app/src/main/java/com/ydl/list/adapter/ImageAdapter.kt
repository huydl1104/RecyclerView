package com.ydl.list.adapter

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ydl.list.data.PictureData
import com.ydl.list.viewHolder.ImageViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter

class ImageAdapter(val mContext: Context) :
    BaseRecyclerAdapter<PictureData, ImageViewHolder>(mContext) {
    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(parent)
    }

    override fun convert(holder: ImageViewHolder, t: PictureData) {
        Glide.with(mContext).load(t.image).into(holder.imgPicture)
    }
}