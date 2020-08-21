package com.ydl.list.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ydl.list.data.AddInfo
import com.ydl.list.data.PersonData
import com.ydl.list.data.PictureData
import com.ydl.list.viewHolder.AdViewHolder
import com.ydl.list.viewHolder.ImageViewHolder
import com.ydl.list.viewHolder.PersonViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.viewholder.BaseViewHolder
import java.security.InvalidParameterException
import java.util.*

class PersonWithAdAdapter(val mContext: Context) :
    BaseRecyclerAdapter<Any, BaseViewHolder>(mContext) {

    companion object {
        const val TYPE_INVALID = 0
        const val TYPE_AD = 1
        const val TYPE_PERSON = 2
    }

    override fun getViewType(position: Int): Int {
        if (getItem(position) is AddInfo) {
            return TYPE_AD
        } else if (getItem(position) is PersonData) {
            return TYPE_PERSON
        }
        return TYPE_INVALID
    }


    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_PERSON -> PersonViewHolder(parent)
            TYPE_AD -> AdViewHolder(parent)
            TYPE_INVALID -> ImageViewHolder(parent)
            else -> throw InvalidParameterException()
        }

    }

    override fun convert(holder: BaseViewHolder, t: Any) {
        when (holder) {
            is PersonViewHolder -> {
                var data = t as PersonData
                if (data.getPersonName() == null || data.getPersonSign() == null) {
                    return
                }
                holder.tvTitle.text = data.getPersonName()
                holder.tvContent.text = data.getPersonSign()
                Glide.with(mContext).load(data.getPersonImage()).into(holder.newsImage!!)
            }
            is AdViewHolder -> {
                val data = t as AddInfo
                val imageView = holder.itemView as ImageView
                Glide.with(mContext).load(data.getImage()).into(imageView)
            }
            is ImageViewHolder -> {
                val data = t as PictureData

                Glide.with(mContext).load(data.image).into(holder.imgPicture)
            }
        }
    }
}