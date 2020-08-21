package com.ydl.list.activity

import android.os.Bundle
import android.os.Handler
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.ydl.list.R
import com.ydl.list.adapter.SpanTypeAdapter
import com.ydl.list.data.SpanModel
import com.ydl.list.utils.Utils
import com.ydl.listlib.itemline.SpaceViewItemLine
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*

class SpanRecyclerViewActivity :AppCompatActivity(){

    private var adapter: SpanTypeAdapter? = null
    private var handler: Handler? = Handler()
    private var mDataList: ArrayList<SpanModel>? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initData()
        initRecyclerView()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
    }


    private fun initData() {
        mDataList = ArrayList<SpanModel>()
        for (i in 0..49) {
            val model = SpanModel()
            model.setName(i.toString() + "")
            if (i == 0) {
                model.setType(6)
            } else if (i <= 7) {
                model.setType(3)
            } else if (i >= 8 && i < 11) {
                model.setType(3)
            } else if (i >= 13 && i < 16) {
                model.setType(5)
            } else {
                model.setType(1)
            }
            mDataList!!.add(model)
        }
    }

    /**
     * 先是定义了一个6列的网格布局，然后通过GridLayoutManager.SpanSizeLookup这个类来动态的指定某个item应该占多少列
     */
    private fun initRecyclerView() {
        val manager = GridLayoutManager(this, 6)
        manager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val model: SpanModel = mDataList!![position]
                return if (model.getType() === 1) {
                    6
                } else if (model.getType() === 2) {
                    3
                } else if (model.getType() === 3) {
                    2
                } else if (model.getType() === 4) {
                    2
                } else {
                    1
                }
            }
        })
        recyclerView.setLayoutManager(manager)
        val itemDecoration = SpaceViewItemLine(Utils.convertDpToPixel(8f, this) as Int)
        itemDecoration.setPaddingEdgeSide(true)
        itemDecoration.setPaddingStart(true)
        recyclerView.addItemDecoration(itemDecoration)
        adapter = SpanTypeAdapter(this)
        recyclerView.setAdapter(adapter)
        adapter!!.setData(mDataList)
    }

}
