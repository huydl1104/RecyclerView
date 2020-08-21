package com.ydl.list.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ydl.list.data.PictureData
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.viewholder.BaseViewHolder

class ImageStageredAdapter : BaseRecyclerAdapter<PictureData, ImageStageredAdapter.ImageViewHolder> {

    private var  mContext :Context ?= null
    constructor(context:Context):super(context){
        this.mContext = context
    }

    inner class ImageViewHolder(parent: ViewGroup) : BaseViewHolder(ImageView(parent.context)) {
        var imgPicture: ImageView = itemView as ImageView

        init {
            imgPicture.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            imgPicture.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun convert(holder: ImageViewHolder, data: PictureData) {
        val params = holder.imgPicture.layoutParams
        val setHeight = holder.getAdapterPosition() % 5
        //计算View的高度
        var height = 300
        when (setHeight) {
            0 -> height = 500
            1 -> height = 750
            2 -> height = 880
            3 -> height = 360
            4 -> height = 660
        }
        params.height = height
        holder. imgPicture.layoutParams = params
        Glide.with(mContext!!)
            .load(data.image)
            .into(holder.imgPicture)
    }

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
       return ImageViewHolder(parent)
    }
}