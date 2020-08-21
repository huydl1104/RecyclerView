package com.ydl.list.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ydl.bannerlib.adapter.StaticPagerAdapter
import com.ydl.bannerlib.hintview.ColorPointHintView
import com.ydl.list.R
import com.ydl.list.adapter.PersonAdapter
import com.ydl.list.data.AddInfo
import com.ydl.list.data.DataProvider
import com.ydl.listlib.interfaces.OnLoadMoreListener
import kotlinx.android.synthetic.main.activity_collapsing.*

class CollapsingActivity :AppCompatActivity() {

    var adapter: PersonAdapter? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapsing)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(PersonAdapter(this).also { adapter = it })
        adapter!!.setMore(R.layout.view_more, object : OnLoadMoreListener {
            override fun onLoadMore() {
                adapter?.addAll(DataProvider.getPersonList(0))
            }
        })
        adapter!!.addAll(DataProvider.getPersonList(0))
        rollPagerView.setHintView(ColorPointHintView(this, Color.YELLOW, Color.GRAY))
        rollPagerView.setAdapter(BannerAdapter())
    }

    private inner class BannerAdapter : StaticPagerAdapter() {

        private val list: List<AddInfo> = DataProvider.getAddInfoList()
        override fun getView(container: ViewGroup?, position: Int): View {
            val imageView =
                ImageView(this@CollapsingActivity)
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(this@CollapsingActivity)
                .load(list[position].getDrawable())
                .into(imageView)
            imageView.setOnClickListener { }
            return imageView
        }

        override fun getCount(): Int = list.size

    }
}