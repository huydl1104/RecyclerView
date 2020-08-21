package com.ydl.list.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
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

class LoadMoreAdapter(
    private val mContext: Context,
    private var hasMore: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    /**
     * 第一种ViewType，正常的item
     */
    private val normalType = 0
    /**
     * 第二种ViewType，底部的提示View
     */
    private val footType = 1
    /**
     * 暴露接口，改变fadeTips的方法
     */
    /**
     * 变量，是否隐藏了底部的提示
     */
    var isFadeTips = false
        private set
    /**
     * 获取主线程的Handler
     */
    private val mHandler = Handler(Looper.getMainLooper())
    private var data:  ArrayList<PersonData>?= null
    fun setData(data: ArrayList<PersonData>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    /**
     * 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
     */
    fun updateList(
        newDatas: ArrayList<PersonData>?,
        hasMore: Boolean
    ) {
        val size = data!!.size
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            data!!.addAll(newDatas)
            this.hasMore = hasMore
            notifyItemRangeInserted(size, newDatas.size)
        }
    }

    /**
     * 自定义方法，获取列表中数据源的最后一个位置，比getItemCount少1，因为不计上footView
     */
    fun getRealLastPosition(): Int{
        return data!!.size
    }

   override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // 根据返回的ViewType，绑定不同的布局文件，这里只有两种
        return if (viewType == normalType) {
            MyViewHolder(
                LayoutInflater.from(mContext)
                    .inflate(R.layout.item_news, parent, false)
            )
        } else { //这个是上拉加载更多的view
            FootHolder(
                LayoutInflater.from(mContext)
                    .inflate(R.layout.view_more, parent, false)
            )
        }
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val person: PersonData = data!![position]
            setBindViewHolder(
                holder as MyViewHolder,
                person,
                position
            )
        } else {
            setFootBindViewHolder(
                holder as FootHolder,
                position
            )
        }
    }

    /**
     * 获取条目数量，之所以要加1是因为增加了一条footView
     */
    override fun getItemCount(): Int{
        return if (data == null) 0 else data!!.size + 1
    }

    /**
     * 根据条目位置返回ViewType，以供onCreateViewHolder方法内获取不同的Holder
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            footType
        } else {
            normalType
        }
    }

    internal inner class MyViewHolder(itemView: View) :
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

    internal inner class FootHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tv_more: TextView = itemView.findViewById(R.id.tv_more)

    }

    private fun setBindViewHolder(
        holder: MyViewHolder,
        person: PersonData,
        position: Int
    ) {
        if (person.getPersonName() == null || person.getPersonName()!!.length === 0) {
            holder.tv_content.text = "data -> $position"
        } else {
            holder.tv_title.setText(person.getPersonName())
        }
        if (!(person.getPersonSign() != null && person.getPersonSign()!!.length !== 0)) {
            holder.tv_content.text = "这个是内容$position"
        } else {
            holder.tv_content.setText(person.getPersonSign())
        }
        Glide.with(holder.iv_news_image.context)
            .load(person.getPersonImage())
            .into(holder.iv_news_image)
    }

    private fun setFootBindViewHolder(
        holder: FootHolder,
        position: Int
    ) { // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个footView
        holder.tv_more.visibility = View.VISIBLE
        // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
        if (hasMore) { // 不隐藏footView提示
            isFadeTips = false
            if (data!!.size > 0) { // 如果查询数据发现增加之后，就显示正在加载更多
                holder.tv_more.text = "正在加载更多..."
            }
        } else {
            if (data!!.size > 0) { // 如果查询数据发现并没有增加时，就显示没有更多数据了
                holder.tv_more.text = "没有更多数据了"
                // 然后通过延时加载模拟网络请求的时间，在500ms后执行
                mHandler.postDelayed(object : Runnable {
                    override fun run() { // 隐藏提示条
                        holder.tv_more.visibility = View.GONE
                        // 将fadeTips设置true
                        isFadeTips = true
                        // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                        hasMore = true
                    }
                }, 500)
            }
        }
    }

}