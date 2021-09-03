package com.ydl.listlib.interfaces

import android.text.Editable

/**
 * @author yudongliang
 * create time 2021-09-02
 * describe : 自动完成的回掉汗水
 */
interface AutoCompleteCallback<T> {

    /**
     * 当列表被点击是调用
     * @param editable : 可编辑的文本
     * @param item : 点击的item
     * @return 若是操作有效返回 true
     */
    fun onPopupItemClicked(editable: Editable, item: T): Boolean

    /**
     * popupWindow 显示状态改变的时候回调
     * @param shown : popupWindow 的显示状态 true：显示；false：隐藏
     */
    fun onPopupVisibilityChanged(shown: Boolean)
}