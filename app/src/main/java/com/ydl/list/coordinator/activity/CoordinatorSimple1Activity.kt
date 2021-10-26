package com.ydl.list.coordinator.activity

import android.content.Context
import android.content.Intent
import com.example.base.BaseActivity
import com.ydl.list.R


class CoordinatorSimple1Activity :BaseActivity() {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, CoordinatorSimple1Activity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_coordinator_simple1

    override fun initView() {

    }

    override fun initData() {

    }
}