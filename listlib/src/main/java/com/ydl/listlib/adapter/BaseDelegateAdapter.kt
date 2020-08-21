package com.ydl.listlib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.ydl.listlib.viewholder.BaseViewHolder

/**
 * Vlayout框架基类适配器
 */
open class BaseDelegateAdapter protected constructor(
    private val mContext: Context,
    layoutHelper: LayoutHelper,
    layoutId: Int,
    count: Int,
    viewTypeItem: Int
) : DelegateAdapter.Adapter<BaseViewHolder>() {
    private val mLayoutHelper: LayoutHelper
    private var mCount = -1
    private var mLayoutId = -1
    private var mViewTypeItem = -1
    fun setData() {}
    override fun onCreateLayoutHelper(): LayoutHelper {
        return mLayoutHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false))
    }
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {}

    override fun getItemViewType(position: Int): Int {
        return mViewTypeItem
    }

    override fun getItemCount(): Int {
        return mCount
    }

    init {
        mCount = count
        mLayoutHelper = layoutHelper
        mLayoutId = layoutId
        mViewTypeItem = viewTypeItem
    }
}