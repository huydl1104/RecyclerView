package com.ydl.list.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ydl.bannerlib.hintview.ColorPointHintView
import com.ydl.bannerlib.view.CustomBannerView
import com.ydl.list.R
import com.ydl.list.adapter.CustomBannerAdapter1
import com.ydl.list.adapter.ImageStageredAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.utils.Utils
import com.ydl.listlib.interfaces.InterfaceItemView
import com.ydl.listlib.interfaces.OnMoreListener
import com.ydl.listlib.itemline.SpaceViewItemLine
import com.ydl.listlib.view.CustomRefreshView
import kotlinx.android.synthetic.main.activity_refresh_view.*


class StaggeredActivity : AppCompatActivity() {
    private var mAdapter: ImageStageredAdapter? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_view)
        mAdapter = ImageStageredAdapter(this)
        recyclerView.setAdapter(mAdapter!!)
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(staggeredGridLayoutManager)
        val itemDecoration = SpaceViewItemLine(20)
        recyclerView.addItemDecoration(itemDecoration)
        mAdapter!!.addHeader(object : InterfaceItemView {
          override fun onCreateView(parent: ViewGroup): View {
                val header = CustomBannerView(this@StaggeredActivity)
                header.setHintView(
                    ColorPointHintView(
                        this@StaggeredActivity,
                        Color.YELLOW,
                        Color.GRAY
                    )
                )
                header.setPlayDelay(2000)
                header.setLayoutParams(
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.convertDpToPixel(200f, this@StaggeredActivity) as Int
                    )
                )
                header.setAdapter(CustomBannerAdapter1(this@StaggeredActivity))
                return header
            }

            override fun onBindView(headerView: View) {}
        })
        mAdapter!!.setMore(R.layout.view_more, object : OnMoreListener {
            override  fun onMoreShow() {
                addData()
            }

            override fun onMoreClick() {}
        })
        mAdapter!!.setNoMore(R.layout.view_nomore)
        recyclerView.setRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                recyclerView.postDelayed(Runnable {
                    mAdapter!!.clear()
                    mAdapter!!.addAll(DataProvider.getPictures())
                }, 1000)
            }
        })
        addData()
    }

    private fun addData() {
        mAdapter!!.addAll(DataProvider.getPictures())
    }
}