package com.ydl.list.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
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
import com.ydl.list.adapter.DeleteAdapter
import com.ydl.list.adapter.NarrowImageAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils
import com.ydl.listlib.interfaces.InterfaceItemView
import com.ydl.listlib.interfaces.OnItemChildClickListener
import com.ydl.listlib.interfaces.OnLoadMoreListener
import com.ydl.listlib.itemline.DividerViewItemLine
import com.ydl.listlib.itemline.RecycleViewItemLine
import com.ydl.listlib.itemline.SpaceViewItemLine
import com.ydl.listlib.swipe.OnSwipeMenuListener
import kotlinx.android.synthetic.main.activity_refresh_view.*

class DeleteAndTopActivity:AppCompatActivity() {
    private var adapter: DeleteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_view)
        recyclerView!!.setAdapter(DeleteAdapter(this).also({ adapter = it }))
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        val itemDecoration = DividerViewItemLine(
            this.resources.getColor(R.color.color_f9f9f9),
            Utils.dip2px(this, 0.5f),
            Utils.dip2px(this, 72f), 0
        )
        itemDecoration.setDrawLastItem(true)
        itemDecoration.setDrawHeaderFooter(true)
        recyclerView!!.addItemDecoration(itemDecoration)
        val line = RecycleViewItemLine(
            this, LinearLayout.HORIZONTAL,
            1, this.resources.getColor(R.color.colorAccent)
        )
        recyclerView!!.addItemDecoration(line)
        recyclerView!!.setRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                adapter!!.clear()
                adapter!!.addAll(DataProvider.getPersonList(0))
            }
        })
        adapter!!.addAll(DataProvider.getPersonList(0))
        adapter!!.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(view: View, position: Int) {
                when (view.id) {
                    R.id.iv_news_image -> Toast.makeText(
                        this@DeleteAndTopActivity,
                        "点击图片了",
                        Toast.LENGTH_SHORT
                    ).show()
                    R.id.tv_title -> Toast.makeText(
                        this@DeleteAndTopActivity,
                        "点击标题",
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> {
                    }
                }
            }
        })
        adapter!!.setOnSwipeMenuListener(object : OnSwipeMenuListener {
            //删除功能
            override fun toDelete(position: Int) {
                if (adapter!!.getAllData().size > position && position > -1) { //移除数据
                    //adapter.getAllData().remove(position);
                    //推荐用这个，刷新adapter
                    //adapter.notifyItemRemoved(position);
                    //这一步，就已经做了上面的两步操作
                    adapter!!.remove(position)
                }
            }

            //置顶功能
            override fun toTop(position: Int) { //先移除那个位置的数据，然后将其添加到索引为0的位置，然后刷新数据
                if (position > 0 && adapter!!.getAllData().size > position) {
                    val person: PersonData = adapter!!.getAllData().get(position)
                    adapter!!.getAllData().remove(person)
                    adapter!!.notifyItemInserted(0)
                    adapter!!.getAllData().add(0, person)
                    adapter!!.notifyItemRemoved(position + 1)
                    if (linearLayoutManager.findFirstVisibleItemPosition() === 0) {
                        recyclerView!!.scrollToPosition(0)
                    }
                }
            }
        })

//        initHeader()

    }


    private fun initHeader() {
        adapter!!.addHeader(object : InterfaceItemView {
            override fun onCreateView(parent: ViewGroup): View {
                val header = CustomBannerView(this@DeleteAndTopActivity)
                header.setHintView(
                    ColorPointHintView(
                        this@DeleteAndTopActivity,
                        Color.YELLOW,
                        Color.GRAY
                    )
                )

                header.setPlayDelay(2000)
                header.setLayoutParams(
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.convertDpToPixel(200f, this@DeleteAndTopActivity) as Int
                    )
                )
                header.setAdapter(CustomBannerAdapter1(this@DeleteAndTopActivity))
                return header
            }

            override fun onBindView(headerView: View) {

                //绑定数据

            }
        })
        adapter!!.addHeader(object : InterfaceItemView {
            override fun onCreateView(parent: ViewGroup): View {
                return LayoutInflater.from(this@DeleteAndTopActivity)
                    .inflate(R.layout.header_view, null)
            }

            override fun onBindView(headerView: View) {


            }
        })
        adapter!!.addHeader(object : InterfaceItemView {
            override fun onCreateView(parent: ViewGroup): View? {
                val recyclerView: RecyclerView = object : RecyclerView(parent.context) {
                    //为了不打扰横向RecyclerView的滑动操作，可以这样处理
                    @SuppressLint("ClickableViewAccessibility")
                    override fun onTouchEvent(event: MotionEvent?): Boolean {
                        super.onTouchEvent(event)
                        return true
                    }
                }
                recyclerView.setLayoutParams(
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.convertDpToPixel(300f, this@DeleteAndTopActivity) as Int
                    )
                )
                var adapter11: NarrowImageAdapter?= null
                recyclerView.setAdapter(NarrowImageAdapter(parent.context).also({ adapter11 = it }))
                recyclerView.setLayoutManager(
                    LinearLayoutManager(
                        parent.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                )
                recyclerView.addItemDecoration(
                    SpaceViewItemLine(
                        Utils.convertDpToPixel(8f, parent.context) as Int
                    )
                )
                adapter11!!.setMore(R.layout.view_more_horizontal, object : OnLoadMoreListener {
                    override fun onLoadMore() {
                        adapter11!!.addAll(DataProvider.getNarrowImage(0))
                    }
                })
                adapter11!!.addAll(DataProvider.getNarrowImage(0))
                return recyclerView
            }

            override fun onBindView(headerView: View) { //这里的处理别忘了
                (headerView as ViewGroup).requestDisallowInterceptTouchEvent(true)
            }
        })
        adapter!!.addFooter(object : InterfaceItemView {
            override fun onCreateView(parent: ViewGroup): View {
                val tv = TextView(this@DeleteAndTopActivity)
                tv.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.convertDpToPixel(56f, this@DeleteAndTopActivity) as Int
                )
                tv.gravity = Gravity.CENTER
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                tv.text = "(-_-)/~~~死宅真是恶心"
                return tv
            }

            override fun onBindView(headerView: View) {

            }
        })
    }
}