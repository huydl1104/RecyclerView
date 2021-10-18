package com.ydl.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter(context: Context?, datas: List<String>?) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    private var mContext: Context? = context
    private var mDatas: List<String>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(
                mContext
            ).inflate(R.layout.item_main, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv.setText(mDatas!![position])
    }

    override fun getItemCount(): Int {
        return mDatas!!.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv: TextView = view.findViewById(R.id.button_text) as TextView

    }
}