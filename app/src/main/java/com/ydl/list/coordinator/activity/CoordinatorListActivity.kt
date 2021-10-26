package com.ydl.list.coordinator.activity

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.example.base.BaseActivity
import com.ydl.list.R
import com.ydl.list.adapter.MainAdapter
import com.ydl.list.adapter.MyClickListener
import kotlinx.android.synthetic.main.activity_coordinator_list.*

class CoordinatorListActivity : BaseActivity() {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,CoordinatorListActivity::class.java))
        }
    }

    val list = arrayListOf(
        "simple",
        "simple1"
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
            0 -> { CoordinatorSimpleActivity.start(this@CoordinatorListActivity) }
            1 -> { CoordinatorSimple1Activity.start(this@CoordinatorListActivity) }
        }
    }

    override fun initData() {

    }
}