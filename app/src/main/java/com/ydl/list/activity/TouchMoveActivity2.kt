package com.ydl.list.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.ydl.list.R
import com.ydl.list.adapter.PersonAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils
import com.ydl.listlib.itemline.RecycleViewItemLine
import com.ydl.listlib.touch.ItemTouchHelpCallback
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*

class TouchMoveActivity2 :AppCompatActivity() {
    private var adapter: PersonAdapter? = null
    private var personList: MutableList<PersonData>? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initRecyclerView()
        initCallBack()
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.setLayoutManager(layoutManager)
        val line = RecycleViewItemLine(
            this, LinearLayout.HORIZONTAL,
            Utils.convertDpToPixel(1f, this) as Int,
            this.resources.getColor(R.color.color_f9f9f9)
        )
        recyclerView.addItemDecoration(line)
        recyclerView.setAdapter(PersonAdapter(this).also({ adapter = it }))
        personList = DataProvider.getPersonList(0)
        adapter!!.addAll(personList!!)
        adapter!!.notifyDataSetChanged()
    }

    private fun initCallBack() {
        val callback = ItemTouchHelpCallback(
            object : ItemTouchHelpCallback.OnItemTouchCallbackListener {
                override fun onSwiped(adapterPosition: Int) { // 滑动删除的时候，从数据库、数据源移除，并刷新UI
                    if (personList != null) { //adapter.remove(adapterPosition);
                        //1、删除数据
                        personList!!.removeAt(adapterPosition)
                        //2、刷新
                        adapter!!.notifyItemRemoved(adapterPosition)
                    }
                }

                override fun onMove(srcPosition: Int, targetPosition: Int): Boolean {
                    if (personList != null) { // 更换数据源中的数据Item的位置
                        Collections.swap(personList, srcPosition, targetPosition)
                        // 更新UI中的Item的位置，主要是给用户看到交互效果
                        adapter!!.notifyItemMoved(srcPosition, targetPosition)
                        return true
                    }
                    return true
                }
            })
        callback.setDragEnable(true)
        callback.setSwipeEnable(true)
        //callback.setColor(this.getResources().getColor(R.color.colorAccent));
        //创建helper对象，callback监听recyclerView item 的各种状态
        val itemTouchHelper = ItemTouchHelper(callback)
        //关联recyclerView，一个helper对象只能对应一个recyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}