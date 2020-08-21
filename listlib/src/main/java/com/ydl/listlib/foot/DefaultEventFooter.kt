package com.ydl.listlib.foot

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ydl.listlib.delegate.DefaultEventDelegate
import com.ydl.listlib.interfaces.InterfaceItemView

class DefaultEventFooter(delegate: DefaultEventDelegate) : InterfaceItemView{

    companion object{
        const val HIDE = 520
        private const val SHOW_MORE = 521
        private const val SHOW_ERROR = 522
        const val SHOW_NO_MORE = 523
    }
    private var mDelegate : DefaultEventDelegate?= null
    private var moreView: View? = null
    private var noMoreView: View? = null
    private var errorView: View? = null
    private var moreViewRes = 0
    private var noMoreViewRes = 0
    private var errorViewRes = 0
    private var flag: Int = HIDE
    //是否展示error的view
    private var skipError = false
    //是否展示noMore的view
    private var skipNoMore = false

    init {
        this.mDelegate = delegate
    }

    override fun onCreateView(parent: ViewGroup): View? {
        var view: View? = null
        when (flag) {
            SHOW_MORE -> {
                if (moreView != null) {
                    view = moreView
                } else if (moreViewRes != 0) {
                    view = LayoutInflater.from(parent.context)
                        .inflate(moreViewRes, parent, false)
                }
                view?.setOnClickListener { mDelegate?.onMoreViewClicked() }
            }
            SHOW_ERROR -> {
                if (errorView != null) {
                    view = errorView
                } else if (errorViewRes != 0) {
                    view = LayoutInflater.from(parent.context)
                        .inflate(errorViewRes, parent, false)
                }
                view?.setOnClickListener { mDelegate?.onErrorViewClicked() }
            }
            SHOW_NO_MORE -> {
                if (noMoreView != null) {
                    view = noMoreView
                } else if (noMoreViewRes != 0) {
                    view = LayoutInflater.from(parent.context)
                        .inflate(noMoreViewRes, parent, false)
                }
                view?.setOnClickListener { mDelegate?.onNoMoreViewClicked() }
            }
            else -> {
            }
        }
        if (view == null) {
            view = FrameLayout(parent.context)
        }
        return view
    }

    override fun onBindView(headerView: View) {
        headerView.post { bindRunning(flag) }
    }

    private fun bindRunning(flag: Int) {
        when (flag) {
            SHOW_MORE -> {
                mDelegate?.onMoreViewShowed()
            }
            SHOW_NO_MORE -> {
                if (!skipNoMore) {
                    mDelegate?.onNoMoreViewShowed()
                }
                skipNoMore = false
            }
            SHOW_ERROR -> {
                if (!skipError) {
                    mDelegate?.onErrorViewShowed()
                }
                skipError = false
            }
            else -> {
            }
        }
    }

    fun showError() {
        skipError = true
        flag = SHOW_ERROR
        val adapter = mDelegate?.mAdapter
        if (adapter != null && adapter.itemCount > 0){
            adapter.notifyItemChanged(adapter.itemCount - 1)
        }
    }

    fun showMore() {
        flag = SHOW_MORE
        val adapter = mDelegate?.mAdapter
        if (adapter != null && adapter.itemCount > 0) {
            adapter.notifyItemChanged(adapter.itemCount - 1)
        }
    }

    fun showNoMore() {
        skipNoMore = true
        flag = SHOW_NO_MORE
        val adapter = mDelegate?.mAdapter
        if (adapter != null && adapter.itemCount > 0) {
            adapter.notifyItemChanged(adapter.itemCount - 1)
        }
    }

    fun hide() {
        flag = HIDE
        val adapter = mDelegate?.mAdapter
        if (adapter != null && adapter.itemCount > 0) {
            adapter.notifyItemChanged(adapter.itemCount - 1)
        }
    }

    fun setMoreView(moreView: View?) {
        this.moreView = moreView
        moreViewRes = 0
    }

    fun setNoMoreView(noMoreView: View?) {
        this.noMoreView = noMoreView
        noMoreViewRes = 0
    }

    fun setErrorView(errorView: View?) {
        this.errorView = errorView
        errorViewRes = 0
    }

    fun setMoreViewRes(moreViewRes: Int) {
        moreView = null
        this.moreViewRes = moreViewRes
    }

    fun setNoMoreViewRes(noMoreViewRes: Int) {
        noMoreView = null
        this.noMoreViewRes = noMoreViewRes
    }

    fun setErrorViewRes(errorViewRes: Int) {
        errorView = null
        this.errorViewRes = errorViewRes
    }

    override fun hashCode(): Int {
        return flag
    }

}