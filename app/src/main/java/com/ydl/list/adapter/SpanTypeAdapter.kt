package com.ydl.list.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.R
import com.ydl.list.data.SpanModel
import com.ydl.listlib.interfaces.OnItemClickListener
import java.util.*

class SpanTypeAdapter(private val mContext: Context) :
    RecyclerView.Adapter<SpanTypeAdapter.MyViewHolder>() {
    private var mListener: OnItemClickListener? = null
    private var data: List<SpanModel>? = ArrayList<SpanModel>()
    fun setData(data: List<SpanModel>?) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getData(): List<SpanModel>? {
        return data
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        this.mListener = listener
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val person: SpanModel = data!![position]
        if (person.getName() == null || person.getName().length === 0) {
            holder.tvContent!!.text = "数据 ->$position"
        } else {
            holder.tvTitle!!.setText(person.getName())
        }
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var viewSparseArray: SparseArray<View?>? = null
        var tvTitle: TextView?
        var tvContent: TextView?

        init {
            if (viewSparseArray == null) {
                viewSparseArray = SparseArray()
            }
            itemView.setBackgroundResource(R.color.colorAccent)
            tvTitle = viewSparseArray!!.get(R.id.tv_title) as TextView?
            tvContent = viewSparseArray!!.get(R.id.tv_content) as TextView?
            if (tvTitle == null) {
                tvTitle = itemView.findViewById(R.id.tv_title)
                viewSparseArray!!.put(R.id.tv_title, tvTitle)
            }
            if (tvContent == null) {
                tvContent = itemView.findViewById(R.id.tv_content)
                viewSparseArray!!.put(R.id.tv_content, tvContent)
            }
            itemView.setOnClickListener {
                mListener?.onItemClick(getAdapterPosition())
            }
        }
    }

    override fun getItemCount(): Int{
        return if (data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_news_content, parent, false)
        return MyViewHolder(view)
    }

}