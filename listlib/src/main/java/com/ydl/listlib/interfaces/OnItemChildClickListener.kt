
package com.ydl.listlib.interfaces

import android.view.View

/**
 * item中点击监听接口
 */
interface OnItemChildClickListener {
    /**
     * item中点击监听接口
     */
    fun onItemChildClick(view: View, position: Int)
}