package com.ydl.listlib.interfaces

import android.view.View

interface InterfaceEventDelegate {
    /**
     * 添加数据 通过长度判断 当前列表的状态
     */
    fun addData(length: Int)

    /**
     * 清除数据
     */
    fun clear()

    /**
     * 停止加载更多
     */
    fun stopLoadMore()

    /**
     * 暂停加载更多
     */
    fun pauseLoadMore()

    /**
     * 恢复加载更多
     */
    fun resumeLoadMore()

    /**
     * 设置加载更多监听
     */
    fun setMore(view: View, listener: OnMoreListener?)

    /**
     * 设置没有更多监听
     */
    fun setNoMore(view: View, listener: OnNoMoreListener?)

    /**
     * 设置加载更多错误监听 发生异常额处理
     */
    fun setErrorMore(view: View, listener: OnErrorListener?)

    /**
     * 设置加载更多监听
     */
    fun setMore(res: Int, listener: OnMoreListener?)

    /**
     * 设置没有更多监听
     */
    fun setNoMore(res: Int, listener: OnNoMoreListener?)

    /**
     * 设置加载更多错误监听
     */
    fun setErrorMore(res: Int, listener: OnErrorListener?)
}