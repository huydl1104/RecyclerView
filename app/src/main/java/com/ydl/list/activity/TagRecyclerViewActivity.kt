package com.ydl.list.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.ydl.list.R
import com.ydl.list.data.DataProvider
import com.ydl.list.utils.Utils
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_recyclerview.*

class TagRecyclerViewActivity:AppCompatActivity() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.setHasFixedSize(true)
        //画笔用来计算文字的宽度
        val mSearchHistoryPaint = Paint()
        mSearchHistoryPaint.textSize = Utils.dip2px(this, 12f).toFloat()
        val width: Int = Utils.getScreenWidth(this)
        val mPaddingSize: Int = Utils.dip2px(this, 40f)
        val gridLayoutManager = GridLayoutManager(this, width)
        gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                //就是这里 需要测量文字的宽度，加上左右内边距
                val textWidth =
                    (mSearchHistoryPaint.measureText(DataProvider.getTags().get(position)).toInt()
                            + mPaddingSize)
                //如果文字的宽度超过屏幕的宽度，那么我们就设置为屏幕宽度
                return if (textWidth > width) width else textWidth
            }
        })
        recyclerView.setLayoutManager(gridLayoutManager)
        val adapter = PersonAdapter(this)
        recyclerView.setAdapter(adapter)
        adapter.addAll(DataProvider.getTags())
    }

    class PersonAdapter internal constructor(context: Context) :
        BaseRecyclerAdapter<String, PersonAdapter.PersonViewHolder>(context) {

        inner class PersonViewHolder internal constructor(parent: ViewGroup) : BaseViewHolder(parent, R.layout.item_tag) {
             val tv: TextView = getView(R.id.tv) as TextView
        }

        override fun convert(holder: PersonViewHolder, t: String) {
            holder.tv.text = t
        }

        override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            return PersonViewHolder(parent)
        }
    }
}