package com.ydl.listlib.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.ydl.listlib.R
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.observer.ViewDataObserver
import java.util.*


/**
 * 支持多种状态切换；支持上拉加载更多，下拉刷新；支持添加头部或底部view
 */
class CustomRefreshView : FrameLayout {

    private var recyclerView: RecyclerView? = null
    private var mProgressView: ViewGroup? = null
    private var mEmptyView: ViewGroup? = null
    private var mErrorView: ViewGroup? = null
    private var mProgressId = 0
    private var mEmptyId = 0
    private var mErrorId = 0
    private var mClipToPadding = false
    private var mPadding = 0
    private var mPaddingTop = 0
    private var mPaddingBottom = 0
    private var mPaddingLeft = 0
    private var mPaddingRight = 0
    private var mScrollbarStyle = 0
    private var mScrollbar = 0
    private var mInternalOnScrollListener: OnScrollListener? = null
    private var mExternalOnScrollListener: OnScrollListener? = null
    private var mExternalOnScrollListenerList = ArrayList<OnScrollListener>()
    private var swipeToRefresh: SwipeRefreshLayout? = null
    private var mRefreshListener: OnRefreshListener? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initAttrs(attrs)
        initView()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (swipeToRefresh != null) {
            swipeToRefresh!!.dispatchTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
    var lastX = -1
    var lastY = -1
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val layManager = recyclerView!!.layoutManager
        if (layManager is LinearLayoutManager){
            val ori = layManager.orientation
            var dealtX = 0
            var dealtY = 0
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    dealtX = 0;
                    dealtY = 0;
                    // 保证子View能够接收到Action_move事件
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                MotionEvent.ACTION_MOVE->{
                    dealtX += Math.abs(x - lastX);
                    dealtY += Math.abs(y - lastY);
                    // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                    if (ori == HORIZONTAL){
                        if (dealtX >= dealtY) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        } else {
                            parent.requestDisallowInterceptTouchEvent(false);
                        }
                        lastX = x;
                        lastY = y;
                    }

                }
                MotionEvent.ACTION_UP->{

                }
            }

        }

        return super.onTouchEvent(event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recyclerView?.removeOnScrollListener(mInternalOnScrollListener!!)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val recyclerView = recyclerView
        if (recyclerView != null) {
            initScrollListener()
            recyclerView.addOnScrollListener(mInternalOnScrollListener!!)
        }
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomRefreshView)
        try {
            mClipToPadding = a.getBoolean(R.styleable.CustomRefreshView_recyclerClipToPadding, false)
            mPadding = a.getDimension(R.styleable.CustomRefreshView_recyclerPadding, -1.0f).toInt()
            mPaddingTop = a.getDimension(R.styleable.CustomRefreshView_recyclerPaddingTop, 0.0f).toInt()
            mPaddingBottom = a.getDimension(R.styleable.CustomRefreshView_recyclerPaddingBottom, 0.0f).toInt()
            mPaddingLeft = a.getDimension(R.styleable.CustomRefreshView_recyclerPaddingLeft, 0.0f).toInt()
            mPaddingRight = a.getDimension(R.styleable.CustomRefreshView_recyclerPaddingRight, 0.0f).toInt()
            mScrollbarStyle = a.getInteger(R.styleable.CustomRefreshView_scrollbarStyle, -1)
            mScrollbar = a.getInteger(R.styleable.CustomRefreshView_scrollbars, -1)
            mEmptyId = a.getResourceId(R.styleable.CustomRefreshView_layout_empty, 0)
            mProgressId = a.getResourceId(R.styleable.CustomRefreshView_layout_progress, 0)
            mErrorId = a.getResourceId(R.styleable.CustomRefreshView_layout_error, 0)
        } finally {
            a.recycle()
        }
    }

    private fun initView() {
        if (isInEditMode) {
            return
        }

        val v = LayoutInflater.from(context).inflate(R.layout.refresh_recyclerview, this)
        swipeToRefresh = v.findViewById(R.id.ptr_layout)
        swipeToRefresh?.isEnabled = false
        mProgressView = v.findViewById(R.id.progress)
        if (mProgressId != 0) {
            LayoutInflater.from(context).inflate(mProgressId, mProgressView)
        }
        mEmptyView = v.findViewById(R.id.empty)
        if (mEmptyId != 0) {
            LayoutInflater.from(context).inflate(mEmptyId, mEmptyView)
        }
        mErrorView = v.findViewById(R.id.error)
        if (mErrorId != 0) {
            LayoutInflater.from(context).inflate(mErrorId, mErrorView)
        }
        initRecyclerView(v)
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(android.R.id.list)
        setItemAnimator(null)
        if (recyclerView != null) {
            recyclerView!!.setHasFixedSize(true)
            recyclerView!!.clipToPadding = mClipToPadding
            if (mPadding != -1) {
                recyclerView!!.setPadding(mPadding, mPadding, mPadding, mPadding)
            } else {
                recyclerView!!.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            }
            if (mScrollbarStyle != -1) {
                recyclerView!!.scrollBarStyle = mScrollbarStyle
            }
            when (mScrollbar) {
                0 -> isVerticalScrollBarEnabled = false
                1 -> isHorizontalScrollBarEnabled = false
                2 -> {
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                }
                else -> {
                }
            }
        }
    }

    private fun initScrollListener() {
        mInternalOnScrollListener = object : OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mExternalOnScrollListener?.onScrolled(recyclerView, dx, dy)
                for (listener in mExternalOnScrollListenerList) {
                    listener.onScrolled(recyclerView, dx, dy)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mExternalOnScrollListener != null) {
                    mExternalOnScrollListener!!.onScrollStateChanged(recyclerView, newState)
                }
                for (listener in mExternalOnScrollListenerList) {
                    listener.onScrollStateChanged(recyclerView, newState)
                }
            }
        }
    }

    fun setLayoutManager(manager: LayoutManager?) {
        if (manager != null) {
            recyclerView!!.layoutManager = manager
        } else {
            throw NullPointerException("un find no manager , please set manager must be null")
        }
    }


    fun setCacheSize(@IntRange size: Int) {
        if (recyclerView != null) {
            recyclerView!!.setItemViewCacheSize(size)
        }
    }

    fun setRecyclerPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mPaddingLeft = left
        mPaddingTop = top
        mPaddingRight = right
        mPaddingBottom = bottom
        recyclerView!!.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
    }


    override fun setClipToPadding(isClip: Boolean) {//是否将剪辑设置为填充
        if (recyclerView != null) {
            recyclerView!!.clipToPadding = isClip
        }
    }


    fun scrollToPosition(position: Int) {
        if (recyclerView != null) {
            recyclerView!!.scrollToPosition(position)
        }
    }

    override fun setVerticalScrollBarEnabled(verticalScrollBarEnabled: Boolean) {
        recyclerView!!.isVerticalScrollBarEnabled = verticalScrollBarEnabled
    }

    override fun setHorizontalScrollBarEnabled(horizontalScrollBarEnabled: Boolean) {
        recyclerView!!.isHorizontalScrollBarEnabled = horizontalScrollBarEnabled
    }

    fun setEmptyView(emptyView: Int) {
        mEmptyView!!.removeAllViews()
        LayoutInflater.from(context).inflate(emptyView, mEmptyView)
    }

    fun setProgressView(progressView: Int) {
        mProgressView!!.removeAllViews()
        LayoutInflater.from(context).inflate(progressView, mProgressView)
    }

    fun setErrorView(errorView: Int) {
        mErrorView!!.removeAllViews()
        LayoutInflater.from(context).inflate(errorView, mErrorView)
    }

    fun setAdapter(adapter: Adapter<*>) {
        recyclerView!!.adapter = adapter
        adapter.registerAdapterDataObserver(ViewDataObserver(this))
        showRecycler()
    }

    fun setAdapterWithProgress(adapter: Adapter<*>) {
        recyclerView!!.adapter = adapter
        adapter.registerAdapterDataObserver(ViewDataObserver(this))
        //只有Adapter为空时才显示ProgressView
        if (adapter is BaseRecyclerAdapter<*,*>) {
            if (adapter.middleCount == 0) {
                showProgress()
            } else {
                showRecycler()
            }
        } else {
            if (adapter.itemCount == 0) {
                showProgress()
            } else {
                showRecycler()
            }
        }
    }

    fun clear() {
        recyclerView!!.adapter = null
    }

    private fun hideAll() {
        mEmptyView!!.visibility = View.GONE
        mProgressView!!.visibility = View.GONE
        mErrorView!!.visibility = View.GONE
        swipeToRefresh!!.isRefreshing = false
        recyclerView!!.visibility = View.INVISIBLE
    }

    fun showError() {
        if (mErrorView!!.childCount > 0) {
            hideAll()
            mErrorView!!.visibility = View.VISIBLE
        } else {
            showRecycler()
        }
    }

    fun showEmpty() {
        if (mEmptyView!!.childCount > 0) {
            hideAll()
            mEmptyView!!.visibility = View.VISIBLE
        } else {
            showRecycler()
        }
    }

    fun showProgress() {
        if (mProgressView!!.childCount > 0) {
            hideAll()
            mProgressView!!.visibility = View.VISIBLE
        } else {
            showRecycler()
        }
    }

    fun showRecycler() {
        hideAll()
        recyclerView!!.visibility = View.VISIBLE
    }

    fun setRefreshListener(listener: OnRefreshListener?) {
        swipeToRefresh!!.isEnabled = true
        swipeToRefresh!!.setOnRefreshListener(listener)
        mRefreshListener = listener
    }

    fun setRefreshing(isRefreshing: Boolean) {
        swipeToRefresh!!.post { swipeToRefresh!!.isRefreshing = isRefreshing }
    }

    fun setRefreshing(
        isRefreshing: Boolean,
        isCallbackListener: Boolean
    ) {
        swipeToRefresh!!.post {
            swipeToRefresh!!.isRefreshing = isRefreshing
            if (isRefreshing && isCallbackListener && mRefreshListener != null) {
                mRefreshListener!!.onRefresh()
            }
        }
    }

    fun setRefreshingColorResources(@ColorRes vararg colRes: Int) {
        swipeToRefresh!!.setColorSchemeResources(*colRes)
    }

    fun setRefreshingColor(vararg col: Int) {
        swipeToRefresh!!.setColorSchemeColors(*col)
    }

    @Deprecated("")
    fun setOnScrollListener(listener: OnScrollListener?) {
        mExternalOnScrollListener = listener
    }

    fun addOnScrollListener(listener: OnScrollListener) {
        mExternalOnScrollListenerList.add(listener)
    }

    fun removeOnScrollListener(listener: OnScrollListener?) {
        mExternalOnScrollListenerList.remove(listener)
    }

    fun removeAllOnScrollListeners() {
        mExternalOnScrollListenerList.clear()
    }


    fun addOnItemTouchListener(listener: OnItemTouchListener?) {//添加条目触摸监听器
        recyclerView!!.addOnItemTouchListener(listener!!)
    }


    fun removeOnItemTouchListener(listener: OnItemTouchListener?) {//移除条目触摸监听器
        recyclerView!!.removeOnItemTouchListener(listener!!)
    }


    val adapter: Adapter<*>? get() = recyclerView!!.adapter

    override fun setOnTouchListener(listener: OnTouchListener) {
        recyclerView!!.setOnTouchListener(listener)
    }


    fun setItemAnimator(animator: ItemAnimator?) {
        recyclerView!!.itemAnimator = animator
    }


    fun addItemDecoration(itemDecoration: ItemDecoration?) {
        recyclerView!!.addItemDecoration(itemDecoration!!)
    }


    fun addItemDecoration(itemDecoration: ItemDecoration?, index: Int) {
        recyclerView!!.addItemDecoration(itemDecoration!!, index)
    }


    fun removeItemDecoration(itemDecoration: ItemDecoration?) {
        recyclerView!!.removeItemDecoration(itemDecoration!!)
    }


    var errorView: View?
        get() = if (mErrorView!!.childCount > 0) {
            mErrorView!!.getChildAt(0)
        } else null
        set(errorView) {
            mErrorView!!.removeAllViews()
            mErrorView!!.addView(errorView)
        }


    var progressView: View?
        get() = if (mProgressView!!.childCount > 0) {
            mProgressView!!.getChildAt(0)
        } else null
        set(progressView) {
            mProgressView!!.removeAllViews()
            mProgressView!!.addView(progressView)
        }


    var emptyView: View?
        get() = if (mEmptyView!!.childCount > 0) {
            mEmptyView!!.getChildAt(0)
        } else null
        set(emptyView) {
            mEmptyView!!.removeAllViews()
            mEmptyView!!.addView(emptyView)
        }
}