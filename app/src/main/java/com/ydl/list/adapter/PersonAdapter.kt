package com.ydl.list.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ydl.list.R
import com.ydl.list.data.PersonData
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.viewholder.BaseViewHolder

class PersonAdapter(var mContext: Context) :
    BaseRecyclerAdapter<PersonData, PersonAdapter.PersonViewHolder>(mContext) {


    inner class PersonViewHolder internal constructor(parent: ViewGroup) :
        BaseViewHolder(parent, R.layout.item_news) {
        var tv_title: TextView
        var iv_news_image: ImageView
        var tv_content: TextView

        init {
            iv_news_image = getView(R.id.iv_news_image) as ImageView
            tv_title = getView(R.id.tv_title) as TextView
            tv_content = getView(R.id.tv_content) as TextView
            addOnClickListener(R.id.iv_news_image)
            addOnClickListener(R.id.tv_title)
        }

    }

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(parent)
    }

    override fun convert(holder: PersonViewHolder, person: PersonData) {
        if (person.getPersonName() == null || person.getPersonName()!!.length === 0) {
            holder.tv_content.text = "test " + 1
        } else {
            holder.tv_title.setText(person.getPersonName())
        }
        if (person.getPersonSign() == null || person.getPersonSign()!!.length === 0) {
            holder.tv_content.text = "这个是内容" + 2
        } else {
            holder.tv_content.text = person.getPersonSign()
        }
        Glide.with(mContext)
            .load(person.getPersonImage())
//                .error(R.drawable.bg_small_tree_min)
//                .placeholder(R.drawable.bg_small_tree_min)
            .into(holder.iv_news_image)

    }
}