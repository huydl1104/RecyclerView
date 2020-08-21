package com.ydl.list.viewHolder

import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ydl.list.R
import com.ydl.list.data.PersonData
import com.ydl.listlib.viewholder.BaseViewHolder
import kotlin.math.log

class PersonViewHolder(val parent: ViewGroup) :
    BaseViewHolder(parent, R.layout.item_news) {

    var tvTitle: TextView
     var newsImage: ImageView?= null
     var tvContent: TextView

    init {
        newsImage = getView(R.id.iv_news_image) as ImageView
        tvTitle = getView(R.id.tv_title) as TextView
        tvContent = getView(R.id.tv_content) as TextView
    }


}