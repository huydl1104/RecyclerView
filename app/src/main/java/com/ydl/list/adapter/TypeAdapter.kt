package com.ydl.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ydl.list.R
import com.ydl.list.data.PersonData
import com.ydl.listlib.interfaces.OnItemClickListener
import java.util.*
import kotlin.collections.ArrayList

class TypeAdapter(private val mContext: Context) :
    RecyclerView.Adapter<TypeAdapter.MyViewHolder?>() {
    private var listener: OnItemClickListener? = null
    private var data: ArrayList<PersonData>? = ArrayList<PersonData>()
    fun setData(data: ArrayList<PersonData>?) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<PersonData>? {
        return data
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

   override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val person: PersonData = data!![position]
        if (person.getPersonName() == null || person.getPersonName()!!.length === 0) {
            holder.tv_content.text = "data ->$position"
        } else {
            holder.tv_title.setText(person.getPersonName())
        }
        if (person.getPersonSign() == null || person.getPersonSign()!!.length === 0) {
            holder.tv_content.text = "这个是内容$position"
        } else {
            holder.tv_content.setText(person.getPersonSign())
        }
        Glide.with(holder.iv_news_image.context)
            .load(person.getPersonImage())
            .into(holder.iv_news_image)
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        val iv_news_image: ImageView = itemView.findViewById(R.id.iv_news_image)
        val tv_content: TextView = itemView.findViewById(R.id.tv_content)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(getAdapterPosition())
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (data == null) 0 else data!!.size
    }

}