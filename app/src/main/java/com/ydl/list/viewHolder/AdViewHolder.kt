package com.ydl.list.viewHolder

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.utils.Utils.convertDpToPixel
import com.ydl.listlib.viewholder.BaseViewHolder

class AdViewHolder(val parent: ViewGroup) :
    BaseViewHolder(ImageView(parent.context)) {

    init {
        val imageView = itemView as ImageView
        imageView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            convertDpToPixel(156f, parent.context)
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}