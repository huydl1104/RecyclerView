
package com.ydl.listlib.observer

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.ydl.listlib.adapter.BaseRecyclerAdapter

class FixDataObserver(private val recyclerView: RecyclerView) : AdapterDataObserver() {
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        if (recyclerView.adapter is BaseRecyclerAdapter<*,*>) {
            val adapter = recyclerView.adapter as BaseRecyclerAdapter<*,*>?
            val footerCount = adapter!!.footerCount
            val count = adapter.middleCount
            if (footerCount > 0 && count == itemCount) {
                recyclerView.scrollToPosition(0)
            }
        }
    }

}