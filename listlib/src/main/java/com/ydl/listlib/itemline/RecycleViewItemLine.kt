
package com.ydl.listlib.itemline

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class RecycleViewItemLine(context: Context, orientation: Int) :
    ItemDecoration() {
    private var mPaint: Paint? = null
    private var mDivider: Drawable?
    /**
     * 分割线高度，默认为1px
     */
    private var mDividerHeight = 1
    /**
     * 列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
     */
    private val mOrientation: Int

    constructor(context: Context, orientation: Int, drawableId: Int) : this(
        context,
        orientation
    ) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        if (mDivider != null) {
            mDividerHeight = mDivider!!.intrinsicHeight
        }
    }

    /**
     * 自定义分割线
     * @param context       上下文
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(
        context: Context, orientation: Int,
        dividerHeight: Int, dividerColor: Int
    ) : this(context, orientation) {
        mDividerHeight = dividerHeight
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        //设置画笔paint的颜色
        mPaint!!.color = dividerColor
        //设置样式
        mPaint!!.style = Paint.Style.FILL
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
        super.getItemOffsets(outRect, view, parent, state)
        //给bottom留出一个高度为mDividerHeight的空白
        //这样做的目的是什么呢？就是为下面onDraw方法绘制高度为mDividerHeight的分割线做准备用的
        //set方法作用：将矩形的坐标设置为指定的值
        outRect[0, 0, 0] = mDividerHeight
    }

    /**
     * 绘制分割线
     * ItemDecoration的onDraw方法是在RecyclerView的onDraw方法中调用的
     * 注意这时候传入的canvas是RecyclerView的canvas，要时刻注意这点，它是和RecyclerView的边界是一致的。
     * 这个时候绘制的内容相当于背景，会被item覆盖。
     * @param c                         canvas用来绘制的画板
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    override fun onDraw(
        c: Canvas, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDraw(c, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    /**
     * 绘制分割线
     * ItemDecoration的onDrawOver方法是在RecyclerView的draw方法中调用的
     * 同样传入的是RecyclerView的canvas，这时候onLayout已经调用，所以此时绘制的内容会覆盖item。
     * @param c                         canvas用来绘制的画板
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    override fun onDrawOver(
        c: Canvas, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDrawOver(c, parent, state)
    }

    /**
     * 绘制横向 item 分割线
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        //获取的当前显示的view的数量，并不会获取不显示的view的数量。
        //假如recyclerView里共有30条数据，而当前屏幕内显示的只有5条，这paren.getChildCount的值是5，不是30。
        val childSize = parent.childCount
        for (i in 0 until childSize) { //获取索引i处的控件view
            val child = parent.getChildAt(i)
            //拿到layoutParams属性
            val layoutParams =
                child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            //使用画笔paint进行绘制
            if (mPaint != null) {
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    mPaint!!
                )
            }
        }
    }

    /**
     * 绘制纵向 item 分割线
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams =
                child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    mPaint!!
                )
            }
        }
    }

    companion object {
        private val ATTRS = intArrayOf(R.attr.listDivider)
    }

    /**
     * 默认分割线：高度为2px，颜色为灰色
     * @param context     上下文
     * @param orientation 列表方向
     */
    init {
        require(
            !(orientation != LinearLayoutManager.VERTICAL &&
                    orientation != LinearLayoutManager.HORIZONTAL)
        ) { "请输入正确的数据！" }
        mOrientation = orientation
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }
}