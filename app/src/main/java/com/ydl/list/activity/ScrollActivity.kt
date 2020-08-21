package com.ydl.list.activity

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.OrientationHelper
import com.ydl.list.R
import com.ydl.list.adapter.ScrollAdapter
import com.ydl.list.app.OnPagerListener
import com.ydl.list.data.DataProvider
import com.ydl.list.manager.PagerLayoutManager
import kotlinx.android.synthetic.main.activity_recyclerview.*

class ScrollActivity:AppCompatActivity(){

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val viewPagerLayoutManager = PagerLayoutManager(this, OrientationHelper.HORIZONTAL)
        val adapter = ScrollAdapter(this)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(viewPagerLayoutManager)
        viewPagerLayoutManager.setOnViewPagerListener(object : OnPagerListener {
           override fun onInitComplete() {
                println("OnPagerListener---onInitComplete--" + "初始化完成")
            }

            override fun onPageRelease(isNext: Boolean, position: Int) {
                println("OnPagerListener---onPageRelease--$position-----$isNext")
            }

            override fun onPageSelected(position: Int, isBottom: Boolean) {
                println("OnPagerListener---onPageSelected--$position-----$isBottom")
            }
        })
        adapter.setData(DataProvider.getPersonList(10))
    }


}
