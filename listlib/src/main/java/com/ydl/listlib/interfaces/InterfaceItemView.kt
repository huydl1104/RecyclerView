
package com.ydl.listlib.interfaces

import android.view.View
import android.view.ViewGroup

/**
 * 持多种状态切换；支持上拉加载更多，下拉刷新；支持添加头部或底部view
 */
interface InterfaceItemView {
    /**
     * 创建view
     */
    fun onCreateView(parent: ViewGroup): View?

    /**
     * 绑定view
     */
    fun onBindView(headerView: View)
}