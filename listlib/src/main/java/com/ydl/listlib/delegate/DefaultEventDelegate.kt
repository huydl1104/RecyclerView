package com.ydl.listlib.delegate

import android.view.View
import com.ydl.listlib.adapter.BaseRecyclerAdapter
import com.ydl.listlib.foot.DefaultEventFooter
import com.ydl.listlib.interfaces.InterfaceEventDelegate
import com.ydl.listlib.interfaces.OnErrorListener
import com.ydl.listlib.interfaces.OnMoreListener
import com.ydl.listlib.interfaces.OnNoMoreListener

class DefaultEventDelegate(adapter: BaseRecyclerAdapter<*,*>) : InterfaceEventDelegate {

    companion object {
        private const val STATUS_INITIAL = 291
        private const val STATUS_MORE = 260
        private const val STATUS_NO_MORE = 408
        private const val STATUS_ERROR = 732
        private var status = STATUS_INITIAL

    }

    var mAdapter: BaseRecyclerAdapter<*,*>? = null
    private var mFooter: DefaultEventFooter? = null

    init {
        this.mAdapter = adapter
        this.mFooter = DefaultEventFooter(this)
    }


    private var hasData = false
    private var isLoadingMore = false
    private var hasMore = false
    private var hasNoMore = false
    private var hasError = false
    private var onMoreListener: OnMoreListener? = null
    private var onNoMoreListener: OnNoMoreListener? = null
    private var onErrorListener: OnErrorListener? = null
    override fun addData(length: Int) {
        if (hasMore) {
            if (length == 0) { //当添加0个时，认为已结束加载到底
                if (status == STATUS_INITIAL || status == STATUS_MORE) {
                    mFooter?.showNoMore()
                    status = STATUS_NO_MORE
                }
            } else { //当Error或初始时。添加数据，如果有More则还原。
                mFooter?.showMore()
                status = STATUS_MORE
                hasData = true
            }
        } else {
            if (hasNoMore) {
                mFooter?.showNoMore()
                status = STATUS_NO_MORE
            }
        }
        isLoadingMore = false
    }

    override fun clear() {
        hasData = false
        status = STATUS_INITIAL
        mFooter?.hide()
        isLoadingMore = false
    }

    override fun stopLoadMore() {
        mFooter?.showNoMore()
        status = STATUS_NO_MORE
        isLoadingMore = false
    }

    override fun pauseLoadMore() {
        mFooter?.showError()
        status = STATUS_ERROR
        isLoadingMore = false
    }

    override fun resumeLoadMore() {
        isLoadingMore = false
        mFooter?.showMore()
        status = STATUS_MORE
        onMoreViewShowed()
    }

    override fun setMore(view: View, listener: OnMoreListener?) {
        hasMore = true
        this.mFooter?.setMoreView(view)
        this.onMoreListener = listener
        if (mAdapter != null && mAdapter!!.middleCount > 0) {
            addData(mAdapter!!.middleCount)
        }
    }

    override fun setMore(res: Int, listener: OnMoreListener?) {
        this.mFooter?.setMoreViewRes(res)
        this.onMoreListener = listener
        hasMore = true
        if (mAdapter != null && mAdapter!!.middleCount > 0) {
            addData(mAdapter!!.middleCount)
        }
    }

    override fun setNoMore(view: View, listener: OnNoMoreListener?) {
        this.mFooter?.setNoMoreView(view)
        this.onNoMoreListener = listener
        hasNoMore = true
    }

    override fun setNoMore(res: Int, listener: OnNoMoreListener?) {
        this.mFooter?.setNoMoreViewRes(res)
        this.onNoMoreListener = listener
        hasNoMore = true
    }

    override fun setErrorMore(view: View, listener: OnErrorListener?) {
        this.mFooter?.setErrorView(view)
        this.onErrorListener = listener
        hasError = true
    }

    override fun setErrorMore(res: Int, listener: OnErrorListener?) {
        this.mFooter?.setErrorViewRes(res)
        this.onErrorListener = listener
        hasError = true
    }

    fun onMoreViewShowed() {
        if (!isLoadingMore && onMoreListener != null) {
            isLoadingMore = true
            onMoreListener?.onMoreShow()
        }
    }

    fun onMoreViewClicked() {
        onMoreListener?.onMoreClick()
    }

    fun onErrorViewShowed() {
        onErrorListener?.onErrorShow()
    }

    fun onErrorViewClicked() {
        onErrorListener?.onErrorClick()
    }

    fun onNoMoreViewShowed() {
        onNoMoreListener?.onNoMoreShow()
    }

    fun onNoMoreViewClicked() {
        onNoMoreListener?.onNoMoreClick()
    }


}