package com.ydl.list.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ydl.list.R
import com.ydl.list.data.DataProvider.getPersonList
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils.dip2px
import com.ydl.list.viewHolder.PersonViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.interfaces.OnErrorListener
import com.ydl.listlib.interfaces.OnItemLongClickListener
import com.ydl.listlib.interfaces.OnLoadMoreListener
import com.ydl.listlib.interfaces.OnNoMoreListener
import com.ydl.listlib.itemline.DividerViewItemLine
import com.ydl.listlib.itemline.StickyHeaderItemLine
import kotlinx.android.synthetic.main.base_recyclerview.*
import java.util.*

class StickyHeaderActivity : AppCompatActivity(), OnLoadMoreListener,
    SwipeRefreshLayout.OnRefreshListener {

    private var adapter: BaseRecyclerAdapter<PersonData,PersonViewHolder>? = null
    private val handler = Handler()
    private var hasNetWork = true
    private val list: ArrayList<Int>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_recyclerview)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        val itemDecoration = DividerViewItemLine(
            this.resources.getColor(R.color.color_f9f9f9),
            dip2px(this, 1f),
            dip2px(this, 72f), 0
        )
        itemDecoration.setDrawLastItem(false)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.setAdapterWithProgress(object : BaseRecyclerAdapter<PersonData,PersonViewHolder>(this) {

            override fun convert(holder: PersonViewHolder, t: PersonData) {
            }

            override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
               return PersonViewHolder(parent)
            }
        }.also { adapter = it })
        adapter!!.setMore(R.layout.view_more, this)
        adapter!!.setNoMore(R.layout.view_nomore, object : OnNoMoreListener {
            override fun onNoMoreShow() {
                adapter!!.resumeMore()
            }

            override fun onNoMoreClick() {
                adapter!!.resumeMore()
            }
        })
        adapter!!.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                adapter!!.remove(position)
                return true
            }
        })
        adapter!!.setError(R.layout.view_error, object : OnErrorListener {
            override fun onErrorShow() {
                adapter!!.resumeMore()
            }

           override fun onErrorClick() {
                adapter!!.resumeMore()
            }
        })
        // StickyHeader
        val decoration = object : StickyHeaderItemLine<HeaderHolder>() {
            override fun getHeaderId(position: Int): Long {
                Log.d("getHeaderId---", position.toString() + "")
                return (position / 3).toLong()
            }

            override fun onCreateHeaderViewHolder(parent: ViewGroup?): HeaderHolder {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.header_item, parent, false)
                return HeaderHolder(view)
            }

            override fun onBindHeaderViewHolder(viewholder: HeaderHolder, position: Int) {
                viewholder.header.text = "第" + getHeaderId(position) + "组"
                Log.d("getHeaderId---", "第" + getHeaderId(position) + "组")
            }

        }
        decoration.setIncludeHeader(true)
        recyclerView.addItemDecoration(decoration)
        top.setOnClickListener(View.OnClickListener { recyclerView.scrollToPosition(0) })
        recyclerView.setRefreshListener(this)
        onRefresh()
    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var header: TextView = itemView as TextView
    }


    //第四页会返回空,意为数据加载结束
    override fun onLoadMore() {
        Log.i("EasyRecyclerView", "onLoadMore")
        handler.postDelayed(Runnable {
            //刷新
            if (!hasNetWork) {
                adapter!!.pauseMore()
                return@Runnable
            }
            adapter!!.addAll(getPersonList(10))
        }, 2000)
    }

    override fun onRefresh() {
        handler.postDelayed(Runnable {
            adapter!!.clear()
            //刷新
            if (!hasNetWork) {
                adapter!!.pauseMore()
                return@Runnable
            }
            adapter!!.addAll(getPersonList(10))
        }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val item = menu.findItem(R.id.checkbox)
        val box = item.actionView as CheckBox
        box.isChecked = true
        box.text = "网络"
        box.setOnCheckedChangeListener { buttonView, isChecked -> hasNetWork = isChecked }
        return true
    }
}