
package com.ydl.listlib.interfaces

/**
 * 上拉加载更多监听
 */
interface OnMoreListener {
    /**
     * 上拉加载更多操作
     */
    fun onMoreShow()

    /**
     * 上拉加载更多操作，手动触发
     */
    fun onMoreClick()
}