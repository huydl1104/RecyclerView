package com.ydl.list.activity

import android.app.Person
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ydl.list.R
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils
import com.ydl.list.viewHolder.PersonViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.interfaces.*
import com.ydl.listlib.itemline.RecycleViewItemLine
import com.ydl.listlib.view.CustomRefreshView
import com.ydl.listlib.viewholder.BaseViewHolder

class RefreshAndMoreActivity1 : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val TAG = "RefreshAndMoreActivity1"
    private var recyclerView: CustomRefreshView? = null
    private var view: FloatingActionButton? = null
    private var mAdapter: BaseRecyclerAdapter<PersonData, PersonViewHolder>? = null
    private var handler = Handler()
    private var page = 0
    private val hasNetWork = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recyclerview1)
        initView()
        onRefresh()
    }

    private fun initView() {
        view = findViewById(R.id.top)
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView?.setLayoutManager(layoutManager)
        val line = RecycleViewItemLine(
            this, LinearLayout.HORIZONTAL, Utils.dip2px(this, 1f),
            this.resources.getColor(R.color.color_f9f9f9)
        )
        recyclerView?.addItemDecoration(line)
        mAdapter = RefreshAdapter(this)
        recyclerView?.setAdapterWithProgress(mAdapter!!)
        mAdapter?.setMore(R.layout.view_more, object : OnLoadMoreListener {
            override fun onLoadMore() {
                handler.postDelayed(Runnable {
                    if (!hasNetWork) {
                        mAdapter!!.pauseMore()
                        return@Runnable
                    }
                    mAdapter?.addAll(DataProvider.getPersonList(10))
                    page++
                }, 1000)
            }
        })
        mAdapter?.setNoMore(R.layout.view_nomore, object : OnNoMoreListener {
            override fun onNoMoreShow() {
                mAdapter!!.pauseMore()
            }

            override fun onNoMoreClick() {
                Log.e(TAG, "没有更多数据了")
            }
        })
        mAdapter!!.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                mAdapter!!.remove(position)
                return true
            }
        })
        mAdapter?.setError(R.layout.view_error, object : OnErrorListener {
            override fun onErrorShow() {
                mAdapter!!.pauseMore()
            }

            override fun onErrorClick() {
                mAdapter!!.resumeMore()
            }
        })
        mAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (mAdapter!!.getAllData().size > position && position >= 0) { //处理点击事件逻辑
                }
            }
        })
        recyclerView?.setRefreshListener(this)
        view?.setOnClickListener { recyclerView?.scrollToPosition(0) }

    }


    override fun onRefresh() {
        page = 0
        handler.postDelayed(Runnable {
            mAdapter!!.clear()
            //刷新
            if (!hasNetWork) {
                mAdapter!!.pauseMore()
                return@Runnable
            }
            mAdapter!!.addAll(DataProvider.getPersonList(10))
            page = 1
            recyclerView!!.showRecycler()
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    inner class RefreshAdapter : BaseRecyclerAdapter<PersonData, PersonViewHolder> {

        private var mContext: Context? = null

        constructor(context: Context) : super(context) {
            this.mContext = context
        }

        override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            return PersonViewHolder(parent)
        }

        override fun convert(holder: PersonViewHolder, data: PersonData) {
            Log.e("yuyu", "PersonViewHolder  setData tvTitle ${data.getPersonName()} ,tvContent ${data.getPersonSign()} ")
            if (data.getPersonName() == null || data.getPersonSign() == null) {
                return
            }
            holder.tvTitle.text = data.getPersonName()
            holder.tvContent.text = data.getPersonSign()
            Glide.with(mContext!!).load(data.getPersonImage()).into(holder.newsImage!!)
        }
    }


}