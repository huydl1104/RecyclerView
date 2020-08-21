package com.ydl.list.viewHolder

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ydl.list.R
import com.ydl.list.data.PictureData
import com.ydl.listlib.viewholder.BaseViewHolder
import java.security.AccessController.getContext

class ImageViewHolder(val parent: ViewGroup) :
    BaseViewHolder(ImageView(parent.context)) {
    var imgPicture: ImageView = itemView as ImageView

    init {
        imgPicture.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
        imgPicture.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}