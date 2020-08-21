
package com.ydl.listlib.viewholder

import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.interfaces.OnItemChildClickListener

open  class BaseViewHolder : ViewHolder {
    private val TAG = "BaseViewHolder"

    private var viewSparseArray: SparseArray<View>? = null
    constructor(itemView: View) : super(itemView) {
        if (viewSparseArray == null) {
            viewSparseArray = SparseArray()
        }
    }

    constructor(parent: ViewGroup, @LayoutRes res: Int) :
        super(LayoutInflater.from(parent.context).inflate(res, parent, false)) {
        if (viewSparseArray == null) {
            viewSparseArray = SparseArray()
        }
    }

    fun  getView(viewId: Int):View {
        var view = viewSparseArray!![viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            viewSparseArray!!.put(viewId, view)
        }
        return view
    }

    /**
     * 获取数据索引的位置
     */
     fun getDataPosition(): Int{
        val adapter = getOwnerAdapter()!!
        if (adapter is BaseRecyclerAdapter<*, *>) {
            val headerCount = adapter.headerCount
            //注意需要减去header的count，否则造成索引错乱
            return adapterPosition - headerCount
        }
        return adapterPosition
    }


    private fun getOwnerAdapter(): RecyclerView.Adapter<*>? {
        return ownerRecyclerView?.adapter
    }


    private val ownerRecyclerView: RecyclerView?  get() {
            try {
                val field = ViewHolder::class.java.getDeclaredField("mOwnerRecyclerView")
                //设置暴力访问权限
                field.isAccessible = true
                return field[this] as RecyclerView
            } catch (ignored: NoSuchFieldException) {
                Log.e(TAG, ignored.localizedMessage)
            } catch (ignored: IllegalAccessException) {
                Log.e(TAG, ignored.localizedMessage)
            }
            return null
        }

    fun addOnClickListener(@IdRes viewId: Int) {
        val view = getView(viewId)
        if (!view.isClickable) {
            view.isClickable = true
        }
        view.setOnClickListener(listener)
    }

    private val listener = View.OnClickListener { v ->
            if (getOwnerAdapter() != null) {
                val onItemChildClickListener: OnItemChildClickListener? =
                    (getOwnerAdapter() as BaseRecyclerAdapter<*,*>).getOnItemChildClickListener()
                onItemChildClickListener?.onItemChildClick(v, getDataPosition())
            }
        }

    fun setText(viewId: Int, text: String?): BaseViewHolder {
        val tv = getView(viewId) as TextView
        tv.text = text
        return this
    }

}