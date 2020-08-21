package com.ydl.list.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ydl.bannerlib.hintview.ColorPointHintView
import com.ydl.bannerlib.view.CustomBannerView
import com.ydl.list.R
import com.ydl.list.adapter.CustomBannerAdapter1
import com.ydl.list.adapter.NarrowImageAdapter
import com.ydl.list.adapter.PersonAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.utils.Utils
import com.ydl.listlib.interfaces.InterfaceItemView
import com.ydl.listlib.interfaces.OnItemChildClickListener
import com.ydl.listlib.interfaces.OnLoadMoreListener
import com.ydl.listlib.itemline.DividerViewItemLine
import com.ydl.listlib.itemline.RecycleViewItemLine
import com.ydl.listlib.itemline.SpaceViewItemLine
import kotlinx.android.synthetic.main.activity_refresh_view.*

class HeaderFooterActivity : AppCompatActivity(){

    private var mAdapter : PersonAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_view)
        mAdapter = PersonAdapter(this)
        recyclerView.setAdapter(mAdapter!!)

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(linearLayoutManager)
        val itemDecoration = DividerViewItemLine(
            this.resources.getColor(R.color.color_f9f9f9),
            Utils.dip2px(this, 0.5f),
            Utils.dip2px(this, 72f), 0
        )
        itemDecoration.setDrawLastItem(true)
        itemDecoration.setDrawHeaderFooter(true)
        recyclerView.addItemDecoration(itemDecoration)

        val line = RecycleViewItemLine(this, LinearLayout.HORIZONTAL, 1, resources.getColor(R.color.colorAccent))
        recyclerView.addItemDecoration(line)

        recyclerView.setRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            recyclerView.postDelayed({
                mAdapter!!.clear()
                mAdapter!!.addAll(DataProvider.getPersonList(0))
            }, 1500)
        })
        recyclerView.setRefreshingColorResources(R.color.colorAccent)
        initHeader()
        mAdapter!!.addAll(DataProvider.getPersonList(0))
        mAdapter!!.setOnItemChildClickListener(object : OnItemChildClickListener {
           override fun onItemChildClick(view: View, position: Int) {
                when (view.id) {
                    R.id.iv_news_image -> Toast.makeText(
                        this@HeaderFooterActivity,
                        "点击图片了", Toast.LENGTH_SHORT
                    ).show()
                    R.id.tv_title -> Toast.makeText(
                        this@HeaderFooterActivity,
                        "点击标题", Toast.LENGTH_SHORT
                    ).show()
                    else -> {
                    }
                }
            }


        })
    }

    private fun initHeader() {
        mAdapter!!.removeAllFooter()
        mAdapter!!.removeAllHeader()
        val interItemView: InterfaceItemView = object : InterfaceItemView {
           override fun onCreateView(parent: ViewGroup): View? {
                val header = CustomBannerView(this@HeaderFooterActivity)
                header.setHintView(ColorPointHintView(this@HeaderFooterActivity, Color.YELLOW, Color.GRAY))
                header.setPlayDelay(2000)
                header.layoutParams = RecyclerView.LayoutParams(
                   ViewGroup.LayoutParams.MATCH_PARENT,
                   Utils.dip2px(this@HeaderFooterActivity,200f )
               )
                header.setAdapter(CustomBannerAdapter1(this@HeaderFooterActivity))
                return header
            }

           override fun onBindView(headerView: View) {

           }
        }
        mAdapter!!.addHeader(interItemView)
        mAdapter!!.addHeader(object : InterfaceItemView {
            override fun onCreateView(parent: ViewGroup): View? {
                return LayoutInflater.from(this@HeaderFooterActivity)
                    .inflate(R.layout.header_view, null,false)
            }

            override fun onBindView(headerView: View) {
                val tvTitle = headerView.findViewById<TextView>(R.id.tvTitle)
            }
        })
        mAdapter!!.addHeader(object : InterfaceItemView {

           override fun onCreateView(parent: ViewGroup): View? {
                val recyclerView: RecyclerView = object : RecyclerView(parent.context) {
                    //为了不打扰横向RecyclerView的滑动操作，可以这样处理
                    @SuppressLint("ClickableViewAccessibility")
                    override fun onTouchEvent(event: MotionEvent?): Boolean {
                        super.onTouchEvent(event)
                        Log.e("yuyu","onTouchEvent  event->$event")
                        return true
                    }
                }
               recyclerView.layoutParams = RelativeLayout.LayoutParams(
                   RelativeLayout.LayoutParams.MATCH_PARENT,
                   Utils.dip2px(this@HeaderFooterActivity,200f)
               )
               var narrowAdapter: NarrowImageAdapter
               recyclerView.adapter = NarrowImageAdapter(parent.context).also {
                   narrowAdapter = it
               }
               recyclerView.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
               recyclerView.addItemDecoration(SpaceViewItemLine(Utils.dip2px(parent.context,8f )))
               narrowAdapter.setMore(R.layout.view_more_horizontal, object : OnLoadMoreListener {
                    override fun onLoadMore() {
                        Handler().postDelayed({ narrowAdapter.addAll(DataProvider.getNarrowImage(0)) }, 1000)
                    }
                })
               narrowAdapter.addAll(DataProvider.getNarrowImage(0))
                return recyclerView
            }

            override  fun onBindView(headerView: View) {
                (headerView as ViewGroup).requestDisallowInterceptTouchEvent(true)
            }
        })
        mAdapter!!.addFooter(object : InterfaceItemView {
            override fun onCreateView(parent: ViewGroup): View {
                return LayoutInflater.from(this@HeaderFooterActivity)
                    .inflate(R.layout.footer_view, null,false)
            }

            override fun onBindView(headerView: View) {

            }

        })
        mAdapter!!.addFooter(object : InterfaceItemView{
            override fun onCreateView(parent: ViewGroup): View? {
                val tv = TextView(this@HeaderFooterActivity)
                tv.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.dip2px(this@HeaderFooterActivity,56f )
                )
                tv.gravity = Gravity.CENTER
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                tv.text = "这个是底部布局"
                return tv
            }

            override fun onBindView(headerView: View) {}
        })
    }

}