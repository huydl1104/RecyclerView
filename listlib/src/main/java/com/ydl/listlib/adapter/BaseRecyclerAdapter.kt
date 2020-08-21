package com.ydl.listlib.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ydl.listlib.delegate.DefaultEventDelegate
import com.ydl.listlib.foot.DefaultEventFooter
import com.ydl.listlib.interfaces.*
import com.ydl.listlib.observer.FixDataObserver
import com.ydl.listlib.viewholder.BaseViewHolder
import com.ydl.listlib.viewholder.GridSpanSizeLookup
import java.util.*

abstract class BaseRecyclerAdapter<T,M :RecyclerView.ViewHolder> : RecyclerView.Adapter<M> {
    //out 和 in 代表 extends 和 super
    private var mObjects: ArrayList<T>? = null
    private var mEventDelegate: InterfaceEventDelegate? = null
    private val headers: ArrayList<InterfaceItemView> = ArrayList()
    private val footers: ArrayList<InterfaceItemView> = ArrayList()
    private var mItemClickListener: OnItemClickListener? = null
    private var mItemLongClickListener: OnItemLongClickListener? = null
    private var onItemChildClickListener: OnItemChildClickListener? = null
    private val mLock = Object()
    private var mNotifyOnChange = true
    private var mContext: Context? = null

    private var mSetHeaderAndFooterSpan = false

    constructor(context: Context) {
        init(context, ArrayList())
    }

    constructor(context: Context, objects: Array<T>) {
        init(context, listOf(*objects))
    }

    constructor(context: Context, objects: List<T>) {
        init(context, objects)
    }

    private fun init(context: Context, objects: List<T>) {
        this.mContext = context
        mObjects = ArrayList(objects)
    }

    /**
     * 页面进入时，显示调用 onAttachedToRecyclerView，隐藏调用 onDetachedFromRecyclerView
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        //增加 容错
        registerAdapterDataObserver(FixDataObserver(recyclerView))
        // 处理 grid模式下的 上拉加载问题
        if (mSetHeaderAndFooterSpan) {
            val manager = recyclerView.layoutManager
            if (manager is GridLayoutManager) {
                manager.spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val itemViewType = getItemViewType(position)
                        //header和footer item data
                        return if (itemViewType >= DefaultEventFooter.HIDE && itemViewType <= DefaultEventFooter.SHOW_NO_MORE) { manager.spanCount }
                        else { 1 }
                    }
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): M {
        val view = createViewByType(parent, viewType)
        if (view != null) {
            return BaseViewHolder(view) as M
        }
        val viewHolder = createCustomViewHolder(parent, viewType)
        setOnClickListener(viewHolder)
        return viewHolder
    }

    //判断当前的item是哪种类型
    @Deprecated("")
    override fun getItemViewType(position: Int): Int {
        if (headers.size != 0) {
            if (position < headers.size) {
                return headers[position].hashCode()
            }
        }
        if (footers.size != 0) {
            val i = position - headers.size - mObjects!!.size
            if (i >= 0) {
                return footers[i].hashCode()
            }
        }
        return getViewType(position - headers.size)
    }

    open fun getViewType(position: Int): Int {
        return 0
    }

    //将数据绑定数据到对应的Item
    override fun onBindViewHolder(holder: M, position: Int) {
        holder.itemView.id = position
        if (headers.size != 0 && position < headers.size) {
            headers[position].onBindView(holder.itemView)
            return
        }
        val i = position - headers.size - mObjects!!.size
        if (footers.size != 0 && i >= 0) {
            footers[i].onBindView(holder.itemView)
            return
        }
        convert(holder,getItem(position - headers.size))
    }

    abstract fun convert(holder: M,t:T)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //通过重写 RecyclerView.onViewRecycled(holder) 来回收资源。
    override fun onViewRecycled(holder: M ) {
        super.onViewRecycled(holder)
    }

    abstract  fun createCustomViewHolder(parent: ViewGroup, viewType: Int): M

    //设置多列数据上拉加载更多时
    fun obtainGridSpanSizeLookUp(maxCount: Int): GridSpanSizeLookup {
        return GridSpanSizeLookup(maxCount, headers, footers, mObjects)
    }

    //设置多列的gridView，需要设置header和footer，以及上拉加载item占一行操作 ,需要在setAdapter之前设置
    fun setHeaderAndFooterSpan(isHeaderAndFooterSpan: Boolean) {
        mSetHeaderAndFooterSpan = isHeaderAndFooterSpan
    }

    //停止加载更多
    fun stopMore() {
        mEventDelegate?.stopLoadMore()
    }

    //暂停加载更多
    fun pauseMore() {
        mEventDelegate?.pauseLoadMore()
    }

    //恢复加载更多
    fun resumeMore() {
        mEventDelegate?.resumeLoadMore()
    }

    //添加headerView
    fun addHeader(view: InterfaceItemView) {
        headers.add(view)
        notifyItemInserted(headers.size - 1)
    }

    //添加footerView
    fun addFooter(view: InterfaceItemView) {
        footers.add(view)
        notifyItemInserted(headers.size + middleCount + footers.size - 1)
    }

    //清除所有header
    fun removeAllHeader() {
        val count = headers.size
        if (count != 0) {
            headers.clear()
            notifyItemRangeRemoved(0, count)
        }

    }

    //清除所有footer
    fun removeAllFooter() {
        val count = footers!!.size
        if (count == 0) {
            return
        }
        footers.clear()
        notifyItemRangeRemoved(headers.size + count, count)
    }

    //获取某个索引处的headerView
    fun getHeader(index: Int): InterfaceItemView? {
        return if (headers.size > 0) {
            headers[index]
        } else {
            null
        }
    }

    //获取某个索引处的footerView
    fun getFooter(index: Int): InterfaceItemView? {
        return if (footers.size > 0) {
            footers[index]
        } else {
            null
        }
    }

    //应该使用这个获取item个数
    val middleCount: Int get() = mObjects!!.size

    //获取header的数量
    val headerCount: Int get() = headers.size

    //获取footer的数量
    val footerCount: Int get() = footers.size

    //包含item+header头布局数量+footer底布局数量
    @Deprecated("")
    override fun getItemCount(): Int {
        return mObjects!!.size + headers.size + footers.size
    }

    //移除某个headerView
    fun removeHeader(view: InterfaceItemView?) {
        val position = headers!!.indexOf(view)
        headers.remove(view)
        notifyItemRemoved(position)
    }

    //移除某个footerView
    fun removeFooter(view: InterfaceItemView?) {
        val position = headers.size + middleCount + footers.indexOf(view)
        footers.remove(view)
        notifyItemRemoved(position)
    }

    private val eventDelegate: InterfaceEventDelegate get() {
            if (mEventDelegate == null) {
                mEventDelegate = DefaultEventDelegate(this)
            }
            return mEventDelegate!!
    }

    //设置上拉加载更多的布局 res 和监听
    fun setMore(res: Int, listener: OnLoadMoreListener) {
        eventDelegate.setMore(res, object : OnMoreListener {
            override fun onMoreShow() {
                listener.onLoadMore()
            }

            override fun onMoreClick() {}
        })
    }

    //设置上拉加载更多的布局view和监听
    fun setMore(view: View, listener: OnLoadMoreListener) {
        eventDelegate.setMore(view, object : OnMoreListener {
            override fun onMoreShow() {
                listener.onLoadMore()
            }

            override fun onMoreClick() {}
        })
    }

    //设置上拉加载更多的自定义布局
    fun setMore(res: Int, listener: OnMoreListener?) {
        Log.e("yuyu","setMore 111 res ")
        eventDelegate.setMore(res, listener)
    }

    //设置上拉加载更多的自定义布局和
    fun setMore(view: View, listener: OnMoreListener?) {
        Log.e("yuyu","setMore 222 view ")
        eventDelegate.setMore(view, listener)
    }

    //设置上拉加载没有更多数据布局
    fun setNoMore(res: Int) {
        eventDelegate.setNoMore(res, null)
    }

    //设置上拉加载没有更多数据布局
    fun setNoMore(view: View) {
        eventDelegate.setNoMore(view, null)
    }

    //设置上拉加载没有更多数据监听
    fun setNoMore(view: View, listener: OnNoMoreListener?) {
        eventDelegate.setNoMore(view, listener)
    }

    //设置上拉加载没有更多数据监听
    fun setNoMore(@LayoutRes res: Int, listener: OnNoMoreListener?) {
        eventDelegate.setNoMore(res, listener)
    }

    //设置上拉加载异常的布局
    fun setError(@LayoutRes res: Int) {
        eventDelegate.setErrorMore(res, null)
    }

    //设置上拉加载异常的布局
    fun setError(view: View) {
        eventDelegate.setErrorMore(view, null)
    }

    //设置上拉加载异常的布局和异常监听
    fun setError(@LayoutRes res: Int, listener: OnErrorListener?) {
        eventDelegate.setErrorMore(res, listener)
    }

    fun setError(view: View, listener: OnErrorListener) {
        eventDelegate.setErrorMore(view, listener)
    }

    fun add(data: T?) {
        if (mEventDelegate != null) {
            mEventDelegate!!.addData(if (data == null) 0 else 1)
        }
        if (data != null) {
            synchronized(mLock) {
                mObjects!!.add(data)
            }
        }
        if (mNotifyOnChange) {
            notifyItemInserted(headers.size + middleCount)
        }
    }

    //添加所有数据
    fun addAll(collection: Collection<T>) {
        if (mEventDelegate != null) {
            mEventDelegate!!.addData(collection.size)
        }
        if (collection.isNotEmpty()) {
            synchronized(mLock) { mObjects!!.addAll(collection) }
        }
        val dataCount = collection.size
        if (mNotifyOnChange) {
            notifyItemRangeInserted(headers.size + middleCount - dataCount, dataCount)
        }
    }

    fun insert(data: T, index: Int) {
        synchronized(mLock) { mObjects!!.add(index,data) }
        if (mNotifyOnChange) {
            notifyItemInserted(headers.size + index)
        }
    }

    fun insertAll(dataArray: Array<T>, index: Int) {
        synchronized(mLock) { mObjects!!.addAll(index, mutableListOf(*dataArray)) }
        val dataCount = dataArray.size
        if (mNotifyOnChange) {
            notifyItemRangeInserted(headers.size + index, dataCount)
        }
    }


    fun insertAll(dataList: Collection<T>, index: Int) {
        synchronized(mLock) { mObjects!!.addAll(index, dataList) }
        val dataCount = dataList.size
        if (mNotifyOnChange) {
            notifyItemRangeInserted(headers.size + index, dataCount)
        }
    }

    fun update(data: T, pos: Int) {
        synchronized(mLock) { mObjects!!.set(pos, data) }
        if (mNotifyOnChange) {
            notifyItemChanged(pos)
        }
    }

    fun remove(data: T) {
        val position = mObjects!!.indexOf(data)
        synchronized(mLock) {
            if (mObjects!!.remove(data)) {
                if (mNotifyOnChange) {
                    notifyItemRemoved(headers.size + position)
                }
            }
        }
    }

    //将某个索引处的数据置顶
    fun setTop(position: Int) {
        var t: T
        synchronized(mLock) {
            t = mObjects!![position]
            mObjects!!.removeAt(position)
        }
        if (mNotifyOnChange) {
            notifyItemInserted(headers.size)
        }
        mObjects!!.add(0, t)
        if (mNotifyOnChange) {
            notifyItemRemoved(headers.size + 1)
        }
    }

    fun remove(position: Int) {
        synchronized(mLock) { mObjects!!.removeAt(position) }
        if (mNotifyOnChange) {
            notifyItemRemoved(headers.size + position)
        }
    }


    fun clear() {
        val count = mObjects!!.size
        if (mEventDelegate != null) {
            mEventDelegate!!.clear()
        }
        synchronized(mLock) { mObjects!!.clear() }
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    //使用指定的比较器对此适配器的内容进行排序
    fun sort(comparator: Comparator<T>?) {
        synchronized(mLock) { Collections.sort(mObjects!!, comparator) }
        if (mNotifyOnChange) {
            notifyDataSetChanged()
        }
    }

    //设置操作数据[增删改查]后，是否刷新adapter
    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }

    private fun createViewByType(parent: ViewGroup, viewType: Int): View? {
        for (headerView in headers) {
            if (headerView.hashCode() == viewType) {
                val view = headerView.onCreateView(parent)
                val layoutParams: StaggeredGridLayoutManager.LayoutParams
                layoutParams = if (view!!.layoutParams != null) {
                    StaggeredGridLayoutManager.LayoutParams(view.layoutParams)
                } else {
                    StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                layoutParams.isFullSpan = true
                view.layoutParams = layoutParams
                return view
            }
        }
        for (footerView in footers) {
            if (footerView.hashCode() == viewType) {
                val view = footerView.onCreateView(parent)
                val layoutParams: StaggeredGridLayoutManager.LayoutParams
                layoutParams = if (view!!.layoutParams != null) {
                    StaggeredGridLayoutManager.LayoutParams(view.layoutParams)
                } else {
                    StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                layoutParams.isFullSpan = true
                view.layoutParams = layoutParams
                return view
            }
        }
        return null
    }

    //获取所有的数据list集合
    fun getAllData(): ArrayList<T> = ArrayList(mObjects!!)

    fun getItem(position: Int):T {
        return mObjects!![position]
    }

    fun getDataPosition(item: T): Int {
        return mObjects!!.indexOf(item)
    }

    //itemView 的点击事件
    private fun setOnClickListener(viewHolder: M) {
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener {
                mItemClickListener!!.onItemClick(viewHolder.adapterPosition - headers.size)
            }
        }
        if (mItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener {
                mItemLongClickListener!!.onItemLongClick(viewHolder.adapterPosition - headers.size)
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }


    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        mItemLongClickListener = listener
    }
    fun setOnItemChildClickListener(listener: OnItemChildClickListener) {
        this.onItemChildClickListener = listener
    }

    fun getOnItemChildClickListener(): OnItemChildClickListener? {
        return onItemChildClickListener
    }

}