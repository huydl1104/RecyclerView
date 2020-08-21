package com.ydl.list.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ydl.list.R
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils
import com.ydl.list.viewHolder.PersonViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.interfaces.OnErrorListener
import com.ydl.listlib.interfaces.OnItemLongClickListener
import com.ydl.listlib.interfaces.OnMoreListener
import com.ydl.listlib.interfaces.OnNoMoreListener
import com.ydl.listlib.itemline.RecycleViewItemLine
import com.ydl.listlib.view.CustomRefreshView
import com.ydl.listlib.viewholder.BaseViewHolder

class RefreshAndMoreActivity2 :AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var recyclerView: CustomRefreshView? = null
    private var top: FloatingActionButton? = null
    private var adapter: BaseRecyclerAdapter<PersonData,PersonViewHolder>? = null
    private var handler = Handler()

    private var page = 0
    private var hasNetWork = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recyclerview1)
        initView()
        onRefresh()
    }


    private fun initView() {
        top = findViewById(R.id.top)
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView?.setLayoutManager(layoutManager)
        val line = RecycleViewItemLine(
            this, LinearLayout.HORIZONTAL,
            Utils.dip2px(this,1f),
            this.resources.getColor(R.color.color_f9f9f9)
        )
        recyclerView?.addItemDecoration(line)
        adapter = RefreshAdapter2(this)
        adapter!!.setHeaderAndFooterSpan(true)
        recyclerView?.setAdapterWithProgress(adapter!!)
        adapter!!.setMore(R.layout.view_more, object : OnMoreListener {
            override fun onMoreShow() { //点击触发加载下一页数据
                Log.e("yuyu","onMoreShow   -----")
                handler.postDelayed(Runnable {
                    if (!hasNetWork) {
                        adapter!!.pauseMore()
                        return@Runnable
                    }
                    adapter!!.addAll(DataProvider.getPersonList(10))
                }, 1000)
            }

            override fun onMoreClick() {}
        })
        adapter!!.setNoMore(R.layout.view_nomore, object : OnNoMoreListener{
            override fun onNoMoreShow() {
                Log.e("yuyu","onMoreShow   -----")
                adapter!!.pauseMore()
            }

            override fun onNoMoreClick() {
                Log.e("yuyu", "没有更多数据了")
            }
        })
        adapter!!.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                Log.e("yuyu","onItemLongClick   -----")
//                adapter!!.remove(position)
                Toast.makeText(this@RefreshAndMoreActivity2,"长按位置$position",Toast.LENGTH_SHORT).show()
                return true
            }
        })
        adapter?.setError(R.layout.view_error, object : OnErrorListener {
            override fun onErrorShow() {
                Log.e("yuyu","onErrorShow   -----")
                Toast.makeText(this@RefreshAndMoreActivity2,"onErrorShow -> resumeMore",Toast.LENGTH_SHORT).show()
                adapter!!.resumeMore()
            }

            override fun onErrorClick() {
                Log.e("yuyu","onErrorClick   -----")
                Toast.makeText(this@RefreshAndMoreActivity2,"onErrorClick -> resumeMore",Toast.LENGTH_SHORT).show()
                adapter!!.resumeMore()
            }
        })
        recyclerView!!.setRefreshListener(this)
        top!!.setOnClickListener { recyclerView!!.scrollToPosition(0) }
    }

    override fun onRefresh() {
        handler.postDelayed(Runnable {
            adapter!!.clear()
            Log.e("yuyu","onRefresh  hasNetWork > $hasNetWork")
            if (!hasNetWork) {
                adapter!!.pauseMore()
                return@Runnable
            }
//            adapter!!.add(PersonData())
            adapter!!.addAll(DataProvider.getPersonList(10))
            recyclerView!!.showRecycler()
        }, 1000)
    }

    inner class  RefreshAdapter2 :BaseRecyclerAdapter<PersonData,PersonViewHolder>{

        private var mContext :Context?= null
        constructor( context: Context):super(context){
            this.mContext = context
        }

        override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            return PersonViewHolder(parent)
        }

        override fun convert(holder: PersonViewHolder, data: PersonData) {
        Log.e("yuyu","PersonViewHolder  setData tvTitle ${data.getPersonName()} ,tvContent ${data.getPersonSign()} ")
        if (data.getPersonName()== null ||data.getPersonSign() ==null){
            return
        }
            holder. tvTitle.text = data.getPersonName()
            holder.tvContent.text = data.getPersonSign()
            Glide.with(mContext!!).load(data.getPersonImage()).into(holder.newsImage!!)
        }

    }
}
