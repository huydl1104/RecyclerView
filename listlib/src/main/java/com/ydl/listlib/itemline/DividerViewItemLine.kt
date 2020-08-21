
package com.ydl.listlib.itemline

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ydl.listlib.adapter.BaseRecyclerAdapter

class DividerViewItemLine : ItemDecoration {
    private var mColorDrawable: ColorDrawable
    /**
     * 分割线的高度，单位是像素px
     */
    private var mHeight: Int
    /**
     * 距离左边的padding值
     */
    private var mPaddingLeft = 0
    /**
     * 距离右边的padding值
     */
    private var mPaddingRight = 0
    /**
     * 设置是否绘制最后一条item的分割线
     */
    private var mDrawLastItem = true
    /**
     * 设置是否绘制header和footer的分割线
     */
    private var mDrawHeaderFooter = false

    constructor(color: Int, height: Int) {
        mColorDrawable = ColorDrawable(color)
        mHeight = height
    }

    constructor(color: Int, height: Int, paddingLeft: Int, paddingRight: Int) {
        mColorDrawable = ColorDrawable(color)
        mHeight = height
        mPaddingLeft = paddingLeft
        mPaddingRight = paddingRight
    }

    fun setDrawLastItem(mDrawLastItem: Boolean) {
        this.mDrawLastItem = mDrawLastItem
    }

    fun setDrawHeaderFooter(mDrawHeaderFooter: Boolean) {
        this.mDrawHeaderFooter = mDrawHeaderFooter
    }

    /**
     * 调用的是getItemOffsets会被多次调用，在layoutManager每次测量可摆放的view的时候回调用一次，
     * 在当前状态下需要摆放多少个view这个方法就会回调多少次。
     * @param outRect                   核心参数，这个rect相当于item摆放的时候设置的margin，
     * rect的left相当于item的marginLeft，
     * rect的right相当于item的marginRight
     * @param view                      当前绘制的view，可以用来获取它在adapter中的位置
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        var orientation = 0
        var headerCount = 0
        var footerCount = 0
        if (parent.adapter == null) {
            return
        }
        //获取header和footer的数量
        if (parent.adapter is BaseRecyclerAdapter<*,*>) {
            headerCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.headerCount
            footerCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.footerCount
        }
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager) {
            orientation = layoutManager.orientation
        } else if (layoutManager is GridLayoutManager) {
            orientation = layoutManager.orientation
        } else if (layoutManager is LinearLayoutManager) {
            orientation = layoutManager.orientation
        }
        val itemCount = parent.adapter!!.itemCount
        val count = itemCount - footerCount
        if (mDrawHeaderFooter) {
            if (position in headerCount until count) {
                if (orientation == OrientationHelper.VERTICAL) { //当是竖直方向的时候，距离底部marginBottom是分割线的高度
                    outRect.bottom = mHeight
                } else {
                    outRect.right = mHeight
                }
            }
        }
    }

    override fun onDrawOver(
        c: Canvas, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.adapter == null) {
            return
        }
        var orientation = 0
        var headerCount = 0
        var footerCount = 0
        val dataCount: Int
        if (parent.adapter is BaseRecyclerAdapter<*,*>) {
            headerCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.headerCount
            footerCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.footerCount
            dataCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.middleCount
        } else {
            dataCount = parent.adapter!!.itemCount
        }
        val dataStartPosition = headerCount
        val dataEndPosition = headerCount + dataCount
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager) {
            orientation = layoutManager.orientation
        } else if (layoutManager is GridLayoutManager) {
            orientation = layoutManager.orientation
        } else if (layoutManager is LinearLayoutManager) {
            orientation = layoutManager.orientation
        }
        val start: Int
        val end: Int
        if (orientation == OrientationHelper.VERTICAL) {
            start = parent.paddingLeft + mPaddingLeft
            end = parent.width - parent.paddingRight - mPaddingRight
        } else {
            start = parent.paddingTop + mPaddingLeft
            end = parent.height - parent.paddingBottom - mPaddingRight
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            //数据项除了最后一项 数据项最后一项
            //header&footer且可绘制
            val a = position >= dataStartPosition
            val b = position < dataEndPosition - 1
            val d = position == dataEndPosition - 1 && mDrawLastItem
            val f =
                position !in dataStartPosition until dataEndPosition && mDrawHeaderFooter
            if (a) {
                if (b || d || f) {
                    if (orientation == OrientationHelper.VERTICAL) {
                        val params =
                            child.layoutParams as RecyclerView.LayoutParams
                        val top = child.bottom + params.bottomMargin
                        val bottom = top + mHeight
                        mColorDrawable.setBounds(start, top, end, bottom)
                        mColorDrawable.draw(c)
                    } else {
                        val params =
                            child.layoutParams as RecyclerView.LayoutParams
                        val left = child.right + params.rightMargin
                        val right = left + mHeight
                        mColorDrawable.setBounds(left, start, right, end)
                        mColorDrawable.draw(c)
                    }
                }
            }
        }
    }
}