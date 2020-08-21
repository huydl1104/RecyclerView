package com.ydl.list.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ydl.bannerlib.hintview.ColorPointHintView
import com.ydl.bannerlib.view.CustomBannerView
import com.ydl.list.R
import com.ydl.list.adapter.CustomBannerAdapter1
import com.ydl.list.adapter.ImageAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PictureData
import com.ydl.list.utils.Utils
import com.ydl.list.viewHolder.ImageViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.interfaces.InterfaceItemView
import com.ydl.listlib.interfaces.OnMoreListener
import com.ydl.listlib.itemline.SpaceViewItemLine
import com.ydl.listlib.view.CustomRefreshView
import kotlinx.android.synthetic.main.activity_refresh_view.*


class StaggeredGridActivity : AppCompatActivity() {

    private var mAdapter: ImageAdapter? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_view)
        mAdapter = ImageAdapter(this);
        recyclerView.setAdapter(mAdapter!!)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.setSpanSizeLookup(mAdapter!!.obtainGridSpanSizeLookUp(3))
        recyclerView.setLayoutManager(gridLayoutManager)
        val itemDecoration = SpaceViewItemLine(20)
        recyclerView.addItemDecoration(itemDecoration)
        mAdapter!!.addHeader(object : InterfaceItemView {

            override fun onCreateView(parent: ViewGroup): View? {
                val header = CustomBannerView(this@StaggeredGridActivity)
                header.setHintView(
                    ColorPointHintView(
                        this@StaggeredGridActivity,
                        Color.YELLOW,
                        Color.GRAY
                    )
                )
//                header.setHintPadding(0, 0, 0, AppUtils.convertDpToPixel(8, this@SevenStaggeredGridActivity) as Int)
                header.setPlayDelay(2000)
                header.setLayoutParams(
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.convertDpToPixel(200f, this@StaggeredGridActivity) as Int
                    )
                )
                header.setAdapter(CustomBannerAdapter1(this@StaggeredGridActivity))
                return header
            }

            override fun onBindView(headerView: View) {
            }
        })
        mAdapter!!.setMore(R.layout.view_more, object : OnMoreListener {
            override  fun onMoreShow() {
                mAdapter?.addAll(DataProvider.getPictures())
            }

            override fun onMoreClick() {}
        })
        mAdapter!!.setNoMore(R.layout.view_nomore)
        recyclerView.setRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                recyclerView.postDelayed(Runnable {
                    mAdapter?.clear()
                    mAdapter?.addAll(DataProvider.getPictures())
                }, 1000)
            }
        })
        val list =  DataProvider.getPictures();
        Log.e("yuyu","list ->$list.size")
        mAdapter?.addAll(list)
    }

}