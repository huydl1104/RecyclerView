package com.ydl.list.activity

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ydl.list.R
import com.ydl.list.adapter.StaggeredLoadMoreAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import kotlinx.android.synthetic.main.activity_recyclerview.*


class StaggeredLoadMoreActivity : AppCompatActivity() {
    private var adapter: StaggeredLoadMoreAdapter? = null
    private var handler: Handler? = Handler()
    private var lastVisibleItem = 0
    private val PAGE_COUNT = 10
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initRecyclerView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
    }

    private fun initRecyclerView() {
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(staggeredGridLayoutManager)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                @NonNull outRect: Rect, @NonNull view: View,
                @NonNull parent: RecyclerView, @NonNull state: RecyclerView.State) {

                super.getItemOffsets(outRect, view, parent, state)
                val position: Int = parent.getChildAdapterPosition(view)
                var spanCount = 0
                var spanIndex = 0
                val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder> = parent.getAdapter() as RecyclerView.Adapter<RecyclerView.ViewHolder>
                val layoutManager: RecyclerView.LayoutManager? = parent.getLayoutManager()
                if (adapter == null || layoutManager == null) {
                    return
                }
                if (layoutManager is StaggeredGridLayoutManager) {
                    spanCount = (layoutManager as StaggeredGridLayoutManager).getSpanCount()
                    spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).getSpanIndex()
                }
                //普通Item的尺寸
                //TODO 会出现错位的问题
                val itemCount: Int = adapter.getItemCount()
                val childCount: Int = layoutManager.getChildCount()
                Log.d("yuyu","SpaceViewItemLine--count--$itemCount-----$childCount---索引--$position---$spanIndex")
                if (position < itemCount && spanCount == 2) {
                    if (spanIndex != GridLayoutManager.LayoutParams.INVALID_SPAN_ID) { //getSpanIndex方法不管控件高度如何，始终都是左右左右返回index
                        if (spanIndex % 2 == 0) { //这个是左边item
                            outRect.left = 20
                            outRect.right = 5
                        } else { //这个是右边item
                            outRect.left = 5
                            outRect.right = 20
                        }
                        if (childCount == 1 || childCount == 2) {
                            outRect.top = 0
                        } else {
                            outRect.top = 20
                        }
                    }
                    //outRect.top = space;
                    Log.d("yuyu","SpaceViewItemLine--间距--" + spanIndex + "----" + outRect.left + "-----" + outRect.right)
                }
            }
        })
        adapter = StaggeredLoadMoreAdapter(this, false)
        recyclerView.setAdapter(adapter)
        // 实现上拉加载重要步骤，设置滑动监听器，RecyclerView自带的ScrollListener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
           override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 在newState为滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 如果没有隐藏footView，那么最后一个条目的位置就比我们的getItemCount少1，自己可以算一下
                    if (!adapter!!.getIsFadeTips() && lastVisibleItem + 1 == adapter!!.getItemCount()) {
                        handler!!.postDelayed({
                            updateRecyclerView(
                                adapter!!.getRealLastPosition(),
                                adapter!!.getRealLastPosition() + PAGE_COUNT
                            )
                        }, 2500)
                    }
                    // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目就要比getItemCount要少2
                    if (adapter!!.getIsFadeTips() && lastVisibleItem + 2 == adapter!!.getItemCount()) {
                        handler!!.postDelayed({
                            updateRecyclerView(
                                adapter!!.getRealLastPosition(),
                                adapter!!.getRealLastPosition() + PAGE_COUNT
                            )
                        }, 2500)
                    }
                }
            }

           override  fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 在滑动完成后，拿到最后一个可见的item的位置
                val positions: IntArray =
                    staggeredGridLayoutManager.findLastVisibleItemPositions(null)
                for (pos in positions) {
                    if (pos > lastVisibleItem) {
                        lastVisibleItem = pos //得到最后一个可见的item的position
                    }
                }
            }
        })
    }

    private fun initData() {
        adapter?.setData(DataProvider.getPersonList(10))
    }

    private fun updateRecyclerView(fromIndex: Int, toIndex: Int) {
        val newDatas: List<PersonData> = DataProvider.getPersonList(0)
        if (newDatas.isNotEmpty()) {
            adapter?.updateList(newDatas, true)
        } else {
            adapter?.updateList(null, false)
        }
    }
}