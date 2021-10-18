package com.ydl.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.BaseActivity
import com.hjq.bar.OnTitleBarListener
import com.hjq.toast.ToastUtils
import com.ydl.list.activity.*
import com.ydl.list.adapter.MainAdapter
import com.ydl.list.adapter.MyClickListener
import com.ydl.list.aop.AopActivity
import com.ydl.list.ui.activity.DialogActivity
import com.ydl.list.ui.activity.ImageSelectActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.activity_recycler.recyclerView

class MainActivity : BaseActivity() {

    val list = arrayListOf(
        "RecyclerView列表",
        "aopTest",
        "browser",
        "Camera",
        "dialog"
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
                   3->{
                       ImageSelectActivity.start(this@MainActivity,
                           object : ImageSelectActivity.OnPhotoSelectListener {
                               override fun onSelected(data: List<String?>) {
                                   ToastUtils.show("选择了$data")
                               }

                               override fun onCancel() {
                                   ToastUtils.show("取消了")
                               }
                           }) }
                    4->{customStartActivity(DialogActivity::class.java)}
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            adapter = mainAdapter
        }
    }

    override fun initData() {

    }

    private fun customStartActivity(java: Class<*>) =
        startActivity(Intent(this, java))
}