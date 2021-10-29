package com.ydl.list

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.example.base.BaseActivity
import com.hjq.toast.ToastUtils
import com.ydl.list.activity.*
import com.ydl.list.adapter.MainAdapter
import com.ydl.list.adapter.MyClickListener
import com.ydl.list.aop.AopActivity
import com.ydl.list.coordinator.activity.CoordinatorListActivity
import com.ydl.list.ui.activity.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.activity_recycler.recyclerView

class MainActivity : BaseActivity() {

    val list = arrayListOf(
        "RecyclerView",
        "aopTest",
        "browser",
        "Camera",
        "dialog",
        "Guide",
        "CoordinatorList",
        "blurView",
        "test"
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        val mainAdapter = MainAdapter(list).apply {
            listener = object : MyClickListener {
                override fun onClick(postion: Int) {
                    jump(postion)
                }
            }
        }
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            adapter = mainAdapter
        }
    }

    private fun jump(postion: Int){
        when(postion){
            0 -> customStartActivity(RecyclerActivity::class.java)
            1 -> customStartActivity(AopActivity::class.java)
            2 -> BrowserActivity.start(this@MainActivity, "https://www.baidu.com")
            3->{ ImageSelectActivity.start(this@MainActivity,
                object : ImageSelectActivity.OnPhotoSelectListener {
                    override fun onSelected(data: List<String?>) { ToastUtils.show("选择了$data") }
                    override fun onCancel() { ToastUtils.show("取消了") }
                }) }
            4->{ customStartActivity(DialogActivity::class.java) }
            5->{ GuideActivity.start(this@MainActivity) }
            6->{ CoordinatorListActivity.start(this@MainActivity)}
            7->{ BlurViewActivity.start(this@MainActivity)}
            8->{ customStartActivity(TestWorkActivity::class.java)}

        }
    }
    override fun initData() {

    }

    private fun customStartActivity(java: Class<*>) =
        startActivity(Intent(this, java))
}