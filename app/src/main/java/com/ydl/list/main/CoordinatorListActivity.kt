package com.ydl.list.main

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.example.base.BaseActivity
import com.ydl.list.R
import com.ydl.list.activity.CollapsingActivity
import com.ydl.list.adapter.MainAdapter
import com.ydl.list.adapter.MyClickListener
import com.ydl.list.coordinator.activity.*
import kotlinx.android.synthetic.main.activity_coordinator_list.*

class CoordinatorListActivity : BaseActivity() {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, CoordinatorListActivity::class.java))
        }
    }

    val list = arrayListOf(
        "simple",
        "simple1",
        "simple2",
        "simple3",
        "simple4",
        "simple5"
    )

    override fun getLayoutId(): Int = R.layout.activity_coordinator_list

    override fun initView() {
        val mainAdapter = MainAdapter(list).apply {
            listener = object : MyClickListener {
                override fun onClick(postion: Int) {
                   jump(postion)
                }
            }
        }

        coordinator.apply {
            layoutManager = GridLayoutManager(this@CoordinatorListActivity,2)
            adapter = mainAdapter
        }
    }

    private fun jump(postion: Int) {
        when(postion){
            0 -> {
                CoordinatorSimpleActivity.start(this@CoordinatorListActivity)
            }
            1 -> {
                CoordinatorSimple1Activity.start(this@CoordinatorListActivity)
            }
            2 -> {
                CoordinatorSimple2Activity.start(this@CoordinatorListActivity)
            }
            3 -> { CollapsingActivity.start(this@CoordinatorListActivity)}
            4 -> { startActivity(Intent(this, PartOneActivity::class.java))}
            5 -> { startActivity(Intent(this, PartTwoActivity::class.java))}
        }
    }

    override fun initData() {

    }
}