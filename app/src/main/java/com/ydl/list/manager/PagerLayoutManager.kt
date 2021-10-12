package com.ydl.list.manager

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.ydl.bannerlib.snap.ScrollPageHelper
import com.ydl.list.listener.OnPagerListener

class PagerLayoutManager @JvmOverloads constructor(
    context: Context?,
    private val mOrientation: Int,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, mOrientation, reverseLayout) {

    private var mRecyclerView: RecyclerView? = null
    private var mPagerSnapHelper: ScrollPageHelper? = null
    private var mOnViewPagerListener: OnPagerListener? = null

    companion object {
        private const val HORIZONTAL = OrientationHelper.HORIZONTAL
        private const val VERTICAL = OrientationHelper.VERTICAL
    }

    init {
        initData()
    }
    private var mDrift = 0//用来判断移动方向

    private fun initData() {
        mPagerSnapHelper = when (mOrientation) {
            HORIZONTAL -> ScrollPageHelper(Gravity.START, false)
            else -> ScrollPageHelper(Gravity.TOP, false)
        }
    }

    /**
     * attach到window窗口时，该方法必须调用
     */
    override fun onAttachedToWindow(recyclerView: RecyclerView) {
        super.onAttachedToWindow(recyclerView)
        this.mRecyclerView = recyclerView
        if (mPagerSnapHelper == null) {
            initData()
        }
        mPagerSnapHelper!!.attachToRecyclerView(mRecyclerView)
        mRecyclerView!!.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener)
    }

    /**
     * 销毁的时候调用该方法，需要移除监听事件
     */
    override fun onDetachedFromWindow(view: RecyclerView, recycler: Recycler) {
        super.onDetachedFromWindow(view, recycler)
        if (mRecyclerView != null) {
            mRecyclerView!!.removeOnChildAttachStateChangeListener(mChildAttachStateChangeListener)
        }
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
    }

    /**
     * 滑动状态的改变
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING
     * 快速滚动-> SCROLL_STATE_SETTLING
     * 空闲状态-> SCROLL_STATE_IDLE
     */
    override fun onScrollStateChanged(state: Int) {
        when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                val viewIdle = mPagerSnapHelper!!.findSnapView(this)
                var positionIdle = 0
                if (viewIdle != null) {
                    positionIdle = getPosition(viewIdle)
                }
                val childCount = childCount
                if (mOnViewPagerListener != null && childCount == 1) {
                    mOnViewPagerListener!!.onPageSelected(
                        positionIdle,
                        positionIdle == childCount - 1
                    )
                }
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                val viewDrag = mPagerSnapHelper!!.findSnapView(this)
                if (viewDrag != null) {
                    val positionDrag = getPosition(viewDrag)
                }
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
                val viewSettling = mPagerSnapHelper!!.findSnapView(this)
                if (viewSettling != null) {
                    val positionSettling = getPosition(viewSettling)
                }
            }
        }
    }

    /**
     * 监听竖直方向的相对偏移量
     * @param dy                                y轴滚动值
     * @param recycler                          recycler
     * @param state                             state滚动状态
     * @return                                  int值
     */
    override fun scrollVerticallyBy(
        dy: Int, recycler: Recycler,
        state: RecyclerView.State
    ): Int {
        if (childCount == 0 || dy == 0) {
            return 0
        }
        mDrift = dy
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    /**
     * 监听水平方向的相对偏移量
     * @param dx                                x轴滚动值
     * @param recycler                          recycler
     * @param state                             state滚动状态
     * @return                                  int值
     */
    override fun scrollHorizontallyBy(
        dx: Int, recycler: Recycler,
        state: RecyclerView.State
    ): Int {
        if (childCount == 0 || dx == 0) {
            return 0
        }
        mDrift = dx
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    /**
     * 设置监听
     * @param listener                      listener
     */
    fun setOnViewPagerListener(listener: OnPagerListener?) {
        mOnViewPagerListener = listener
    }

    private val mChildAttachStateChangeListener: OnChildAttachStateChangeListener =
        object : OnChildAttachStateChangeListener {
            /**
             * 第一次进入界面的监听，可以做初始化方面的操作
             */
            override fun onChildViewAttachedToWindow(view: View) {
                if (mOnViewPagerListener != null && childCount == 1) {
                    mOnViewPagerListener!!.onInitComplete()
                }
            }

            /**
             * 页面销毁的时候调用该方法，可以做销毁方面的操作
             */
            override fun onChildViewDetachedFromWindow(view: View) {
                if (mDrift >= 0) {
                    if (mOnViewPagerListener != null) {
                        mOnViewPagerListener!!.onPageRelease(true, getPosition(view))
                    }
                } else {
                    if (mOnViewPagerListener != null) {
                        mOnViewPagerListener!!.onPageRelease(false, getPosition(view))
                    }
                }
            }
        }


}