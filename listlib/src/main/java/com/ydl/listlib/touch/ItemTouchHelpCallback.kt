
package com.ydl.listlib.touch

import android.R
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ItemTouchHelpCallback(
    private val onItemTouchCallbackListener: OnItemTouchCallbackListener?
) :
    ItemTouchHelper.Callback() {
    /**
     * 是否可以拖拽
     */
    private var isCanDrag = false
    /**
     * 是否可以被滑动
     */
    private var isCanSwipe = false
    /**
     * 按住拖动item的颜色
     */
    private var color = 0

    /**
     * 设置是否可以被拖拽
     *
     * @param canDrag 是true，否false
     */
    fun setDragEnable(canDrag: Boolean) {
        isCanDrag = canDrag
    }

    /**
     * 设置是否可以被滑动
     *
     * @param canSwipe 是true，否false
     */
    fun setSwipeEnable(canSwipe: Boolean) {
        isCanSwipe = canSwipe
    }

    /**
     * 设置按住拖动item的颜色
     * @param color     颜色
     */
    fun setColor(@ColorInt color: Int) {
        this.color = color
    }

    /**
     * 当Item被长按的时候是否可以被拖拽
     *
     * @return                      true
     */
    override fun isLongPressDragEnabled(): Boolean {
        return isCanDrag
    }

    /**
     * Item是否可以被滑动(H：左右滑动，V：上下滑动)
     * isItemViewSwipeEnabled()返回值是否可以拖拽排序，true可以，false不可以
     * @return                      true
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return isCanSwipe
    }

    /**
     * 当用户拖拽或者滑动Item的时候需要我们告诉系统滑动或者拖拽的方向
     * 动作标识分：dragFlags和swipeFlags
     * dragFlags：列表滚动方向的动作标识（如竖直列表就是上和下，水平列表就是左和右）
     * wipeFlags：与列表滚动方向垂直的动作标识（如竖直列表就是左和右，水平列表就是上和下）
     *
     * 若果你不想上下拖动，可以将 dragFlags = 0
     * 如果你不想左右滑动，可以将 swipeFlags = 0
     * 最终的动作标识（flags）必须要用makeMovementFlags()方法生成
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) { // flag如果值是0，相当于这个功能被关闭
            val dragFlag = (ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN)
            val swipeFlag = 0
            return makeMovementFlags(dragFlag, swipeFlag)
        } else if (layoutManager is LinearLayoutManager) {
            val orientation = layoutManager.orientation
            var dragFlag = 0
            var swipeFlag = 0
            // 为了方便理解，相当于分为横着的ListView和竖着的ListView
            // 如果是横向的布局
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                swipeFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                dragFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else if (orientation == LinearLayoutManager.VERTICAL) { // 如果是竖向的布局，相当于ListView
                dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
            //第一个参数是拖拽flag，第二个是滑动的flag
            return makeMovementFlags(
                dragFlag,
                swipeFlag
            )
        }
        return 0
    }

    /**
     * 当Item被拖拽的时候被回调
     *
     * @param recyclerView          recyclerView
     * @param srcViewHolder         当前被拖拽的item的viewHolder
     * @param targetViewHolder      当前被拖拽的item下方的另一个item的viewHolder
     * @return                      是否被移动
     */
    override fun onMove(
        recyclerView: RecyclerView,
        srcViewHolder: ViewHolder,
        targetViewHolder: ViewHolder
    ): Boolean {
        if (onItemTouchCallbackListener != null) {
            val srcPosition = srcViewHolder.adapterPosition
            val targetPosition = targetViewHolder.adapterPosition
            return onItemTouchCallbackListener.onMove(srcPosition, targetPosition)
        }
        return false
    }

    /**
     * 当item侧滑出去时触发（竖直列表是侧滑，水平列表是竖滑）
     *
     * @param viewHolder            viewHolder
     * @param direction             滑动的方向
     */
    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        onItemTouchCallbackListener?.onSwiped(viewHolder.adapterPosition)
    }

    /**
     * 当item被拖拽或侧滑时触发
     *
     * @param viewHolder            viewHolder
     * @param actionState           当前item的状态
     */
    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        //不管是拖拽或是侧滑，背景色都要变化
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (color == 0) {
                viewHolder!!.itemView.setBackgroundColor(
                    viewHolder.itemView.context.resources.getColor(R.color.darker_gray)
                )
            } else {
                viewHolder!!.itemView.setBackgroundColor(color)
            }
        }
    }

    /**
     * 当item的交互动画结束时触发
     */
    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.setBackgroundColor(
            viewHolder.itemView.context.resources
                .getColor(R.color.white)
        )
        viewHolder.itemView.alpha = 1f
        viewHolder.itemView.scaleY = 1f
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val value = 1 - Math.abs(dX) / viewHolder.itemView.width
            viewHolder.itemView.alpha = value
            viewHolder.itemView.scaleY = value
        }
    }

    interface OnItemTouchCallbackListener {
        /**
         * 当某个Item被滑动删除的时候
         */
        fun onSwiped(adapterPosition: Int)

        /**
         * 当两个Item位置互换的时候被回调
         * @param srcPosition       拖拽的item的position
         * @param targetPosition    目的地的Item的position
         */
        fun onMove(srcPosition: Int, targetPosition: Int): Boolean
    }

}