package com.ydl.list.activity

import android.os.Bundle
import android.os.Handler
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.R
import com.ydl.list.adapter.LoadMoreAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils
import com.ydl.listlib.interfaces.OnItemClickListener
import com.ydl.listlib.itemline.DividerViewItemLine
import kotlinx.android.synthetic.main.activity_recyclerview.*

class LoadMoreActivity :AppCompatActivity(){

    private var adapter: LoadMoreAdapter? = null
    private var handler: Handler? = Handler()
    private val PAGE_COUNT = 10

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initRecyclerView()
        initData()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        val itemDecoration = DividerViewItemLine(
            this.resources.getColor(R.color.color_f9f9f9)
            , Utils.dip2px(this, 1f),
            Utils.dip2px(this, 30f), 30
        )
        recyclerView.addItemDecoration(itemDecoration)
        adapter = LoadMoreAdapter(this, false)
        recyclerView.setAdapter(adapter)
        adapter!!.setOnClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {}
        })
        // 实现上拉加载重要步骤，设置滑动监听器，RecyclerView自带的ScrollListener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //用来标记是否正在向上滑动
            private var isSlidingUpward = false

            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 当不滑动的时候
                // 在newState为滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //获取最后一个完全显示的itemPosition
                    val lastItemPosition: Int =
                        layoutManager.findLastCompletelyVisibleItemPosition()
                    val itemCount: Int = layoutManager.getItemCount()
                    //int itemCount1 = adapter.getItemCount();
                    // 判断是否滑动到了最后一个item，并且是向上滑动
                    if (lastItemPosition == itemCount - 1 && isSlidingUpward) {
                        handler!!.postDelayed({
                            updateRecyclerView(
                                adapter!!.getRealLastPosition(),
                                adapter!!.getRealLastPosition() + PAGE_COUNT
                            )
                        }, 2000)
                    }
                }
            }

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                isSlidingUpward = dy > 0
            }
        })
    }


    private fun initData() {
        handler!!.postDelayed({ adapter!!.setData(DataProvider.getPersonList(10)) }, 50)
    }

    private fun updateRecyclerView(fromIndex: Int, toIndex: Int) {
        val newDatas=DataProvider.getPersonList(0)
        if (newDatas.size > 0) {
            adapter!!.updateList(newDatas, true)
        } else {
            adapter!!.updateList(null, false)
        }
    }


}
