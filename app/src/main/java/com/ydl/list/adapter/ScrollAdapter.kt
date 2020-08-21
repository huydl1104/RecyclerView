package com.ydl.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.R
import com.ydl.list.data.PersonData
import com.ydl.listlib.interfaces.OnItemClickListener
import java.util.*

class ScrollAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ScrollAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var data: List<PersonData> = ArrayList<PersonData>()
    fun setData(data: List<PersonData>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }


    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_image_pager, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        @NonNull holder: MyViewHolder,
        position: Int
    ) {
        holder.imageView.setBackgroundResource(data[position].getPersonImage()!!)
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.iv_image)
    }

   override  fun getItemCount(): Int = data.size

}