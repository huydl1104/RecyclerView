package com.ydl.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydl.list.activity.*
import com.ydl.list.adapter.MainAdapter
import com.ydl.list.adapter.MyClickListener
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity(){


    val list = arrayListOf(
        "下拉刷新，上拉加载更多，自动加载下一页",
        "下拉刷新，上拉加载更多，双列数据",
        "支持头部和底部自定义布局的刷新控件",
        "支持插入或者删除数据",
        "多种type复杂类型页面",
        "粘贴头部list，采用ItemDecoration实现",
        "粘贴头部list，采用CoordinatorLayout实现",
        "原生控件实现，采用ItemDecoration实现",
        "规则瀑布流效果1",
        "不规则瀑布流效果2",
        "不规则瀑布流效果3",
        "RecyclerView条目Item拖拽排序与滑动删除1",
        "RecyclerView条目Item拖拽排序与滑动删除2",
        "支持侧滑逻辑，左右滑动出现删除按钮",
        "recyclerView的setSpanSizeLookup",
        "复杂type页面的UI效果，支持ItemTouchHelper",
        "recyclerView的上拉加载更多操作",
        "使用阿里vlayout控件实现复杂页面",
        "主页面",
        "流标签",
        "整页滑动",
        "EditText下拉列表"
    )

    private val mainAdapter = MainAdapter(list).apply {
        listener = object :MyClickListener{
            override fun onClick(postion: Int) {
                when(postion){
                    0->{customStartActivity(RefreshAndMoreActivity1::class.java)}
                    1->{customStartActivity(RefreshAndMoreActivity2::class.java)}
                    2->{customStartActivity(HeaderFooterActivity::class.java)}
                    3->{customStartActivity(InsertRemoveActivity::class.java)}
                    4->{customStartActivity(MultiStyleActivity::class.java)}
                    5->{customStartActivity(StickyHeaderActivity::class.java)}
                    6->{customStartActivity(StickyViewActivity::class.java)}
                    7->{customStartActivity(StickyNormalActivity::class.java)}
                    8->{customStartActivity(StaggeredGridActivity::class.java)}
                    9->{customStartActivity(StaggeredActivity::class.java)}
                    10->{customStartActivity(StaggeredLoadMoreActivity::class.java)}
                    11->{customStartActivity(TouchMoveActivity::class.java)}
                    12->{customStartActivity(TouchMoveActivity2::class.java)}
                    13->{customStartActivity(DeleteAndTopActivity::class.java)}
                    14->{customStartActivity(SpanRecyclerViewActivity::class.java)}
                    15->{customStartActivity(TypeActivity::class.java)}
                    16->{customStartActivity(LoadMoreActivity::class.java)}
                    17->{customStartActivity(LayoutActivity::class.java)}
                    18->{customStartActivity(HomeActivity::class.java)}
                    19->{customStartActivity(TagRecyclerViewActivity::class.java)}
                    20->{customStartActivity(ScrollActivity::class.java)}
                    21->{customStartActivity(EditActivity::class.java)}
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@RecyclerActivity,2)
            adapter = mainAdapter
        }

    }

    private fun customStartActivity(java: Class<*>) =
        startActivity(Intent(this, java))

}
