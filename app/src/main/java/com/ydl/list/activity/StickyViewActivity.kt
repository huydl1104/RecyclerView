package com.ydl.list.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.ydl.list.R
import com.ydl.list.adapter.PersonAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.interfaces.OnErrorListener
import com.ydl.listlib.interfaces.OnItemLongClickListener
import com.ydl.listlib.interfaces.OnLoadMoreListener
import com.ydl.listlib.interfaces.OnNoMoreListener
import com.ydl.listlib.itemline.RecycleViewItemLine
import kotlinx.android.synthetic.main.activity_sticky_header.*
import kotlinx.android.synthetic.main.include_scroll_bottom.*


class StickyViewActivity : AppCompatActivity() {

    private var mAdapter: BaseRecyclerAdapter<PersonData,PersonAdapter.PersonViewHolder>? = null
    private val hasNetWork = true
    private var handler: Handler? = Handler()

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class STATES {
        companion object {
            var EXPANDED = 3
            var COLLAPSED = 2
            var INTERMEDIATE = 1
        }
    }

    private var state = 0
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticky_header)
        initCoordinator()
        initRecyclerView()
        initData()
    }

    private fun initCoordinator() {
        appbar?.addOnOffsetChangedListener(listener)
    }

    private val listener: AppBarLayout.OnOffsetChangedListener = object : AppBarLayout.OnOffsetChangedListener {
        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            val totalScrollRange: Int = appBarLayout.getTotalScrollRange()
            if (verticalOffset == 0) {
                if (state != STATES.EXPANDED) { //修改状态标记为展开
                    state = STATES.EXPANDED
                    Log.e("yuyu","OnOffsetChangedListener" + "修改状态标记为展开")
                }
            } else if (Math.abs(verticalOffset) >= totalScrollRange) {
                if (state != STATES.COLLAPSED) { //修改状态标记为折叠
                    state = STATES.COLLAPSED
                    Log.e("yuyu","OnOffsetChangedListener" + "修改状态标记为折叠")
                }
            } else {
                if (state != STATES.INTERMEDIATE) { //修改状态标记为中间
                    state = STATES.INTERMEDIATE
                    //代码设置是否拦截事件
                    Log.e("yuyu","OnOffsetChangedListener" + "修改状态标记为中间")
                }
            }
        }
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
        val line = RecycleViewItemLine(
            this, LinearLayout.HORIZONTAL,
            Utils.convertDpToPixel(1f, this) as Int,
            this.getResources().getColor(R.color.color_f9f9f9)
        )
        recyclerView.addItemDecoration(line)
        mAdapter = PersonAdapter(this)
        recyclerView.setAdapter(mAdapter)
        mAdapter!!.setMore(R.layout.view_more, object : OnLoadMoreListener {
           override fun onLoadMore() {
                handler!!.postDelayed(Runnable {
                    //刷新
                    if (!hasNetWork) {
                        mAdapter?.pauseMore()
                        return@Runnable
                    }
                    mAdapter?.addAll(DataProvider.getPersonList(10))
                }, 2000)
            }
        })
        mAdapter!!.setNoMore(R.layout.view_nomore, object : OnNoMoreListener {
            override fun onNoMoreShow() {
                mAdapter?.pauseMore()
            }

            override fun onNoMoreClick() {}
        })
        mAdapter!!.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                mAdapter?.remove(position)
                return true
            }
        })
        mAdapter!!.setError(R.layout.view_error, object : OnErrorListener {
            override fun onErrorShow() {
                mAdapter?.resumeMore()
            }

            override fun onErrorClick() {
                mAdapter?.resumeMore()
            }
        })
    }

    private fun initData() {
        handler!!.postDelayed(Runnable {
            mAdapter?.clear()
            //刷新
            if (!hasNetWork) {
                mAdapter?.pauseMore()
                return@Runnable
            }
            mAdapter?.addAll(DataProvider.getPersonList(10))
        }, 50)
    }
}
