
package com.ydl.listlib.viewholder

import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.ydl.listlib.interfaces.InterfaceItemView
import java.util.*

class GridSpanSizeLookup(
    private val mMaxCount: Int, private val headers: ArrayList<InterfaceItemView>,
    private val footers: ArrayList<InterfaceItemView>, private val mObjects: ArrayList<*>?
) : SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        if (headers.size != 0) {
            if (position < headers.size) {
                return mMaxCount
            }
        }
        if (footers.size != 0) {
            val i = position - headers.size - mObjects!!.size
            if (i >= 0) {
                return mMaxCount
            }
        }
        return 1
    }

}