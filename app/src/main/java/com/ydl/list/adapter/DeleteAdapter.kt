package com.ydl.list.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ydl.list.R
import com.ydl.list.data.PersonData
import com.ydl.list.viewHolder.PersonViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.swipe.OnSwipeMenuListener
import com.ydl.listlib.viewholder.BaseViewHolder

class DeleteAdapter(val mContext: Context) : BaseRecyclerAdapter<PersonData, DeleteAdapter.PersonViewHolder>(mContext) {

    private var listener: OnSwipeMenuListener? = null
    fun setOnSwipeMenuListener(listener: OnSwipeMenuListener?) {
        this.listener = listener
    }

    inner class PersonViewHolder internal constructor(parent: ViewGroup) :
        BaseViewHolder(parent, R.layout.item_news_del) {
        private val tv_title: TextView
        private val iv_news_image: ImageView
        private val tv_content: TextView
        private val btn_del: Button
        private val btn_top: Button
        fun setData(person: PersonData) {
            tv_title.setText(person.getPersonName())
            tv_content.setText(person.getPersonSign())
            Glide.with(mContext)
                .load(person.getPersonImage())
                .into(iv_news_image)
            val clickListener =
                View.OnClickListener { v ->
                    when (v.id) {
                        R.id.btn_del -> if (null != listener) {
                            listener?.toDelete(getAdapterPosition())
                        }
                        R.id.btn_top -> if (null != listener) {
                            listener?.toTop(getAdapterPosition())
                        }
                    }
                }
            btn_del.setOnClickListener(clickListener)
            btn_top.setOnClickListener(clickListener)
        }

        init {
            iv_news_image = getView(R.id.iv_news_image) as ImageView
            tv_title = getView(R.id.tv_title) as TextView
            tv_content = getView(R.id.tv_content) as TextView
            btn_del = getView(R.id.btn_del) as Button
            btn_top = getView(R.id.btn_top) as Button
            addOnClickListener(R.id.iv_news_image)
            addOnClickListener(R.id.tv_title)
        }
    }

    override fun convert(holder: DeleteAdapter.PersonViewHolder, t: PersonData) {

    }

    override fun createCustomViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):  PersonViewHolder {
        return PersonViewHolder(parent)
    }
}