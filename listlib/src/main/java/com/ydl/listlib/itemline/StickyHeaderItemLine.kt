
package com.ydl.listlib.itemline

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import java.util.*

abstract class StickyHeaderItemLine<VH:ViewHolder>  : ItemDecoration {
    private val mHeaderCache: MutableMap<Long, ViewHolder?>
    private var mRenderInline: Boolean
    private var mIncludeHeader = false
    init {
        mHeaderCache = HashMap()
    }
    constructor():this(false){}
    constructor(renderInline: Boolean){
        this.mRenderInline = renderInline
    }

    abstract fun getHeaderId(position: Int): Long
    abstract fun onCreateHeaderViewHolder(parent: ViewGroup?): VH
    abstract fun onBindHeaderViewHolder(viewholder: VH, position: Int)

    fun setIncludeHeader(mIncludeHeader: Boolean) {
        this.mIncludeHeader = mIncludeHeader
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
        var position = parent.getChildAdapterPosition(view)
        var headerHeight = 0
        if (!mIncludeHeader) {
            if (parent.adapter is BaseRecyclerAdapter<*,*>) {
                val headerCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.headerCount
                val footerCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.footerCount
                val dataCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.middleCount
                if (position < headerCount) {
                    return
                }
                if (position >= headerCount + dataCount) {
                    return
                }
                if (position >= headerCount) {
                    position -= headerCount
                }
            }
        }
        val hasHeader = hasHeader(position)
        val showHeaderAboveItem = showHeaderAboveItem(position)
        if (position != RecyclerView.NO_POSITION && hasHeader && showHeaderAboveItem) {
            val header = getHeader(parent, position)!!.itemView
            headerHeight = getHeaderHeightForLayout(header)
        }
        outRect[0, headerHeight, 0] = 0
    }

    private fun showHeaderAboveItem(itemAdapterPosition: Int): Boolean {
        return if (itemAdapterPosition == 0) { true } else
            getHeaderId(itemAdapterPosition - 1) != getHeaderId(itemAdapterPosition)
    }

    fun clearHeaderCache() {
        mHeaderCache.clear()
    }

    fun findHeaderViewUnder(x: Float, y: Float): View? {
        for (holder in mHeaderCache.values) {
            val child = holder!!.itemView
            val translationX = ViewCompat.getTranslationX(child)
            val translationY = ViewCompat.getTranslationY(child)
            if (x >= child.left + translationX && x <= child.right + translationX && y >= child.top + translationY && y <= child.bottom + translationY
            ) {
                return child
            }
        }
        return null
    }

    /**
     * 判断是否有header
     */
    private fun hasHeader(position: Int): Boolean {
        return getHeaderId(position) != NO_HEADER_ID
    }

    private fun getHeader(parent: RecyclerView, position: Int): ViewHolder? {
        val key = getHeaderId(position)
        return if (mHeaderCache.containsKey(key)) {
            mHeaderCache[key]
        } else {
            val holder = onCreateHeaderViewHolder(parent)
            val header = holder.itemView
            onBindHeaderViewHolder(holder, position)
            val widthSpec = View.MeasureSpec.makeMeasureSpec(
                parent.measuredWidth,
                View.MeasureSpec.EXACTLY
            )
            val heightSpec = View.MeasureSpec.makeMeasureSpec(
                parent.measuredHeight,
                View.MeasureSpec.UNSPECIFIED
            )
            val childWidth = ViewGroup.getChildMeasureSpec(
                widthSpec,
                parent.paddingLeft + parent.paddingRight,
                header.layoutParams.width
            )
            val childHeight = ViewGroup.getChildMeasureSpec(
                heightSpec,
                parent.paddingTop + parent.paddingBottom,
                header.layoutParams.height
            )
            header.measure(childWidth, childHeight)
            header.layout(0, 0, header.measuredWidth, header.measuredHeight)
            mHeaderCache[key] = holder
            holder
        }
    }

    /**
     * 绘制分割线
     * ItemDecoration的onDrawOver方法是在RecyclerView的draw方法中调用的
     * 同样传入的是RecyclerView的canvas，这时候onLayout已经调用，所以此时绘制的内容会覆盖item。
     * @param canvas                    canvas用来绘制的画板
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    override fun onDrawOver(
        canvas: Canvas, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.adapter == null) {
            return
        }
        val count = parent.childCount
        var previousHeaderId: Long = -1
        for (layoutPos in 0 until count) {
            val child = parent.getChildAt(layoutPos)
            var adapterPos = parent.getChildAdapterPosition(child)
            if (!mIncludeHeader) {
                if (parent.adapter is BaseRecyclerAdapter<*,*>) {
                    val headerCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.headerCount
                    val footerCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.footerCount
                    val dataCount = (parent.adapter as BaseRecyclerAdapter<*,*>?)!!.middleCount
                    if (adapterPos < headerCount) {
                        continue
                    }
                    if (adapterPos >= headerCount + dataCount) {
                        continue
                    }
                    if (adapterPos >= headerCount) {
                        adapterPos -= headerCount
                    }
                }
            }
            if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
                val headerId = getHeaderId(adapterPos)
                if (headerId != previousHeaderId) {
                    previousHeaderId = headerId
                    val header = getHeader(parent, adapterPos)!!.itemView
                    canvas.save()
                    val left = child.left
                    val top = getHeaderTop(parent, child, header, adapterPos, layoutPos)
                    canvas.translate(left.toFloat(), top.toFloat())
                    header.translationX = left.toFloat()
                    header.translationY = top.toFloat()
                    header.draw(canvas)
                    canvas.restore()
                }
            }
        }
    }

    private fun getHeaderTop(
        parent: RecyclerView, child: View, header: View,
        adapterPos: Int, layoutPos: Int
    ): Int {
        val headerHeight = getHeaderHeightForLayout(header)
        var top = child.y.toInt() - headerHeight
        if (layoutPos == 0) {
            val count = parent.childCount
            val currentId = getHeaderId(adapterPos)
            // find next view with header and compute the offscreen push if needed
            for (i in 1 until count) {
                val adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i))
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    val nextId = getHeaderId(adapterPosHere)
                    if (nextId != currentId) {
                        val next = parent.getChildAt(i)
                        val offset = next.y.toInt() - (headerHeight +
                                getHeader(parent, adapterPosHere)!!.itemView.height)
                        return if (offset < 0) {
                            offset
                        } else {
                            break
                        }
                    }
                }
            }
            top = Math.max(0, top)
        }
        return top
    }

    private fun getHeaderHeightForLayout(header: View): Int {
        return if (mRenderInline) 0 else header.height
    }

    companion object {
        private const val NO_HEADER_ID = -1L
    }


}