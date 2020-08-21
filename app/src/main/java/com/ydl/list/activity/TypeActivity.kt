package com.ydl.list.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.R
import com.ydl.list.adapter.TypeAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.utils.Utils
import com.ydl.listlib.interfaces.OnItemClickListener
import com.ydl.listlib.itemline.DividerViewItemLine
import com.ydl.listlib.touch.ItemTouchHelpCallback
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*

class TypeActivity :AppCompatActivity() {
    private var adapter: TypeAdapter? = null
    private var handler: Handler? = Handler()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initRecyclerView()
        initData()
        initCallBack()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        val itemDecoration = DividerViewItemLine(
            this.resources.getColor(R.color.color_f9f9f9)
            , Utils.dip2px(this, 1f),
            Utils.dip2px(this, 30f), 30
        )
        val itemDecoration1 = RVItemDecoration(this)
        recyclerView.addItemDecoration(itemDecoration1)
        adapter = TypeAdapter(this)
        recyclerView.setAdapter(adapter)
        adapter!!.setOnClickListener(object : OnItemClickListener {
           override fun onItemClick(position: Int) {}
        })
    }


    private fun initData() {
        handler!!.postDelayed({ adapter!!.setData(DataProvider.getPersonList(0)) }, 50)
    }


    private fun initCallBack() {
        val callback = ItemTouchHelpCallback(
            object : ItemTouchHelpCallback.OnItemTouchCallbackListener {
                override fun onSwiped(adapterPosition: Int) { // 滑动删除的时候，从数据库、数据源移除，并刷新UI
                    if (adapter!!.getData() != null) { //1、删除数据
                        adapter!!.getData()!!.removeAt(adapterPosition)
                        //2、刷新
                        adapter!!.notifyItemRemoved(adapterPosition)
                    }
                }

                override  fun onMove(srcPosition: Int, targetPosition: Int): Boolean {
                    if (adapter!!.getData() != null) { // 更换数据源中的数据Item的位置
                        Collections.swap(adapter!!.getData(), srcPosition, targetPosition)
                        // 更新UI中的Item的位置，主要是给用户看到交互效果
                        adapter!!.notifyItemMoved(srcPosition, targetPosition)
                        return true
                    }
                    return true
                }
            })
        callback.setDragEnable(true)
        callback.setSwipeEnable(true)
        //创建helper对象，callback监听recyclerView item 的各种状态
        val itemTouchHelper = ItemTouchHelper(callback)
        //关联recyclerView，一个helper对象只能对应一个recyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private class RVItemDecoration(context: Context) :
        RecyclerView.ItemDecoration() {
        private val mDrawable: Drawable
        private val marginLeft = 20
        private val lineSize = 10
        private val mPaint: Paint= Paint()

        init {
            mPaint.isAntiAlias = true
            mPaint.color = Color.BLUE
            mDrawable = ColorDrawable(
                ContextCompat.getColor(
                    context.getApplicationContext(),
                    R.color.color_f9f9f9
                )
            )
        }

       override fun onDraw(@NonNull canvas: Canvas, @NonNull parent: RecyclerView, state: RecyclerView.State) {
            canvas.save()
            val childCount: Int = parent.getChildCount()
            for (i in 0 until childCount) {
                val child: View = parent.getChildAt(i)
                val pos: Int = parent.getChildAdapterPosition(child)
                when (pos) {
                    0 -> { //画一半长度的线。上半部缺失。
                        mDrawable.setBounds(
                            marginLeft, (child.top + child.bottom) / 2,
                            marginLeft + lineSize, child.bottom
                        )
                        mDrawable.draw(canvas)
                    }
                    parent.getAdapter()!!.getItemCount() - 1 -> { //画一半长度的线。下半部缺失。
                        mDrawable.setBounds(
                            marginLeft, child.top, marginLeft + lineSize,
                            (child.top + child.bottom) / 2
                        )
                        mDrawable.draw(canvas)
                    }
                    else -> { //画普通的线。
                        mDrawable.setBounds(
                            marginLeft, child.top,
                            marginLeft + lineSize, child.bottom
                        )
                        mDrawable.draw(canvas)
                    }
                }
                //后画覆盖在线上的圆球。
                canvas.drawCircle(
                    marginLeft + lineSize / 2.toFloat(),
                    (child.top +
                            child.bottom) / 2.toFloat(),
                    (lineSize + marginLeft) / 2.toFloat(),
                    mPaint
                )
            }
            canvas.restore()
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect[(marginLeft + lineSize) * 2, 0, 0] = 0
        }


    }

}
