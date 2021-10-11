package com.ydl.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydl.list.activity.*
import com.ydl.list.adapter.MainAdapter
import com.ydl.list.adapter.MyClickListener
import com.ydl.list.aop.AopActivity
import kotlinx.android.synthetic.main.activity_recycler.*

class MainActivity : AppCompatActivity() {

    val list = arrayListOf(
        "RecyclerView列表",
        "aopProject"
    )

    private val mainAdapter = MainAdapter(list).apply {
        listener = object : MyClickListener {
            override fun onClick(postion: Int) {
                when(postion){
                   0 -> customStartActivity(RecyclerActivity::class.java)
                   1 -> customStartActivity(AopActivity::class.java)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }
    }

    private fun customStartActivity(java: Class<*>) =
        startActivity(Intent(this, java))
}