
package com.ydl.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ydl.bannerlib.view.CustomBannerView
import com.ydl.list.R
import com.ydl.list.data.HomePageEntry
import com.ydl.listlib.viewholder.BaseViewHolder

class HomePageAdapter(private val context: Context) : RecyclerView.Adapter<BaseViewHolder>() {
    private var mData: List<HomePageEntry>? = null
    fun setData(data: List<HomePageEntry>?) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_BANNER -> BannerViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_vlayout_banner, parent, false)
            )
            TYPE_AD -> AdViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_vlayout_ad, parent, false)
            )
            TYPE_GRID -> GridViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_vlayout_grid, parent, false)
            )
            TYPE_IMAGE -> ImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_vlayout_title, parent, false)
            )
            TYPE_NEW -> NewViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_vlayout_news, parent, false)
            )
            else ->  {
                ImageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_vlayout_title, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(@NonNull holder: BaseViewHolder, position: Int) {
        val type = getItemViewType(position)
        when (type) {
            TYPE_BANNER ->                 // banner 逻辑处理
                setBanner(holder as BannerViewHolder, position)
            TYPE_AD ->                 // 广告逻辑处理
                setAd(holder, position)
            TYPE_GRID ->                 // 文本逻辑处理
                setGrid(holder, position)
            TYPE_IMAGE ->                 //图片逻辑处理
                setImage(holder, position)
            TYPE_NEW ->                 //视频逻辑处理
                setNew(holder as NewViewHolder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            //banner在开头
            TYPE_BANNER
        } else {
            //type 的值为TYPE_AD，TYPE_IMAGE，TYPE_AD，等其中一个
            mData!![position].getType()
        }
    }

    override fun getItemCount(): Int{
        return if (mData == null) 0 else mData!!.size
    }

    class BannerViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val mBanner: CustomBannerView = itemView.findViewById(R.id.banner)

        init {
            //绑定控件
        }
    }

    class NewViewHolder(itemView: View) : BaseViewHolder(itemView)
    class AdViewHolder(itemView: View) : BaseViewHolder(itemView)
    class GridViewHolder(itemView: View) : BaseViewHolder(itemView)
    class ImageViewHolder(itemView: View) : BaseViewHolder(itemView)

    private fun setBanner(holder: BannerViewHolder, position: Int) {
        // 绑定数据
        val mBanner: CustomBannerView = holder.mBanner
        mBanner.setHintGravity(1)
        mBanner.setAnimationDuration(1000)
        mBanner.setPlayDelay(2000)
        mBanner.setAdapter(CustomBannerAdapter1(context))
    }

    private fun setAd(holder: RecyclerView.ViewHolder, position: Int) {}
    private fun setGrid(holder: RecyclerView.ViewHolder, position: Int) {}
    private fun setImage(holder: RecyclerView.ViewHolder, position: Int) {}
    private fun setNew(holder: NewViewHolder, position: Int) {}

    companion object {
        const val TYPE_BANNER = 0
        const val TYPE_AD = 1
        const val TYPE_GRID = 2
        const val TYPE_IMAGE = 3
        const val TYPE_NEW = 4
    }

}