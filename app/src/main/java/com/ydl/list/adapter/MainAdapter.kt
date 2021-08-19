package com.ydl.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.R
import com.ydl.listlib.adapter.MutableListAdapter

/**
 * @author yudongliang
 * create time 2021-08-19
 * describe : 主界面的配置器
 */
class MainAdapter(var list: MutableList<String> = arrayListOf()) : MutableListAdapter<String, MainAdapter.MyViewHolder>(list){

    var listener: MyClickListener?= null
    
    class MyViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        val textView: Button = itemView.findViewById(R.id.button_text)
    }

    override fun compareItems(checkContent: Boolean, a: String, b: String): Boolean {
        //比较两个 item 或者 内容 是否相等
        return if (checkContent) a == b else a === b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = list[position]
        holder.textView.setOnClickListener{
            listener?.onClick(position)
        }
    }
}

interface MyClickListener{
    fun onClick(postion:Int)
}