package com.ydl.list.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ydl.list.utils.Utils
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.viewholder.BaseViewHolder
import java.security.AccessController.getContext

class NarrowImageAdapter(val mContext: Context) : BaseRecyclerAdapter<Int,NarrowImageAdapter.NarrowImageViewHolder>(mContext) {

    inner class NarrowImageViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var imgPicture: ImageView = itemView as ImageView

        init {
            imgPicture.layoutParams = ViewGroup.LayoutParams(
                Utils.dip2px(mContext,80.0f) ,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imgPicture.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): NarrowImageViewHolder {
        return NarrowImageViewHolder(ImageView(parent.context))
    }


    override fun convert(holder: NarrowImageViewHolder, t: Int) {
        holder.imgPicture.setImageResource(t)
    }
}