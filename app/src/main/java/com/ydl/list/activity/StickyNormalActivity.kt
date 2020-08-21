package com.ydl.list.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ydl.list.R
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils
import com.ydl.list.viewHolder.PersonViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.interfaces.OnItemLongClickListener
import com.ydl.listlib.interfaces.OnLoadMoreListener
import com.ydl.listlib.itemline.DividerViewItemLine
import com.ydl.listlib.itemline.StickyHeaderItemLine
import kotlinx.android.synthetic.main.base_recyclerview.*
import java.util.*


class StickyNormalActivity : AppCompatActivity(), OnLoadMoreListener,
    SwipeRefreshLayout.OnRefreshListener {
    private var adapter: BaseRecyclerAdapter<PersonData,PersonViewHolder>? = null
    private val handler = Handler()
    private var page = 0
    private var hasNetWork = true
    private val list: ArrayList<Int>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_recyclerview)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        val itemDecoration = DividerViewItemLine(
            this.getResources().getColor(R.color.color_f9f9f9),
            Utils.dip2px(this, 1f),
            Utils.dip2px(this, 72f), 0
        )
        itemDecoration.setDrawLastItem(false)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.setAdapterWithProgress(object : BaseRecyclerAdapter<PersonData,PersonViewHolder>(this) {
            override fun convert(holder: PersonViewHolder, t: PersonData) {
            }

            override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
               return  PersonViewHolder(parent)
            }
        }.also { adapter = it })
        adapter!!.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                adapter?.remove(position)
                return true
            }
        })
        // StickyHeader
        val decoration = object : StickyHeaderItemLine<StickyHeaderActivity.HeaderHolder>() {
            override fun getHeaderId(position: Int): Long {
                Log.d("getHeaderId---", position.toString() + "")
                return (position / 3).toLong()
            }

            override fun onCreateHeaderViewHolder(parent: ViewGroup?): StickyHeaderActivity.HeaderHolder {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.header_item, parent, false)
                return StickyHeaderActivity.HeaderHolder(view)
            }

            override fun onBindHeaderViewHolder(viewholder: StickyHeaderActivity.HeaderHolder, position: Int) {
                viewholder.header.text = "第" + getHeaderId(position) + "组"
                Log.d("getHeaderId---", "第" + getHeaderId(position) + "组")
            }

        }
        recyclerView.addItemDecoration(decoration)
        top.setOnClickListener { recyclerView.scrollToPosition(0) }
        recyclerView.setRefreshListener(this)
        onRefresh()
    }

    //第四页会返回空,意为数据加载结束
   override fun onLoadMore() {
        Log.i("EasyRecyclerView", "onLoadMore")
        handler.postDelayed(Runnable {
            //刷新
            if (!hasNetWork) {
                adapter?.pauseMore()
                return@Runnable
            }
            adapter?.addAll(DataProvider.getPersonList(page))
            page++
        }, 2000)
    }

   override fun onRefresh() {
        page = 0
        handler.postDelayed(Runnable {
            adapter?.clear()
            //刷新
            if (!hasNetWork) {
                adapter?.pauseMore()
                return@Runnable
            }
            adapter?.addAll(DataProvider.getPersonList(page))
            page = 1
        }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        val item = menu.findItem(R.id.checkbox)
        val box = item.actionView as CheckBox
        box.isChecked = true
        box.text = "网络"
        box.setOnCheckedChangeListener { buttonView, isChecked -> hasNetWork = isChecked }
        return true
    }
}