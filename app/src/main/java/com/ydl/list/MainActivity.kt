package com.ydl.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.bar.OnTitleBarListener
import com.ydl.list.activity.*
import com.ydl.list.adapter.MainAdapter
import com.ydl.list.adapter.MyClickListener
import com.ydl.list.aop.AopActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.activity_recycler.recyclerView

class MainActivity : AppCompatActivity() {

    val list = arrayListOf(
        "RecyclerView列表",
        "aopTest",
        "browser"
    )

    private val mainAdapter = MainAdapter(list).apply {
        listener = object : MyClickListener {
            override fun onClick(postion: Int) {
                when(postion){
                   0 -> customStartActivity(RecyclerActivity::class.java)
                   1 -> customStartActivity(AopActivity::class.java)
                   2 -> BrowserActivity.start(this@MainActivity,
                       "https://www.baidu.com"
                   )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            adapter = mainAdapter
        }
    }

    private fun customStartActivity(java: Class<*>) =
        startActivity(Intent(this, java))
}