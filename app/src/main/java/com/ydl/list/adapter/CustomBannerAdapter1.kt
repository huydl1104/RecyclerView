package com.ydl.list.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ydl.bannerlib.adapter.StaticPagerAdapter
import com.ydl.list.data.AddInfo
import com.ydl.list.data.DataProvider

class CustomBannerAdapter1(private val ctx: Context) : StaticPagerAdapter() {

    private val list: List<AddInfo> = DataProvider.getAddInfoList()

    override fun getView(container: ViewGroup?, position: Int): View {
        val linear = LinearLayout(ctx)
        val parentParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        linear.layoutParams = parentParams
        val imageView = ImageView(ctx)
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        linear.addView(imageView)

        //加载图片
        Glide.with(ctx)
            .load(list[position].getDrawable())
            .into(imageView)
        //点击事件
        imageView.setOnClickListener { Toast.makeText(ctx,"click --》$position",Toast.LENGTH_SHORT).show()}
        return linear
    }

    override fun getCount(): Int = list.size

}