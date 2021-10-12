package com.ydl.list.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.ydl.bannerlib.view.CustomBannerView
import com.ydl.list.R
import com.ydl.list.adapter.CustomBannerAdapter1
import com.ydl.list.adapter.NarrowImageAdapter
import com.ydl.list.contants.ViewType
import com.ydl.list.data.DataProvider
import com.ydl.listlib.adapter.BaseDelegateAdapter
import com.ydl.listlib.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*

class LayoutActivity:AppCompatActivity() {

    private var layoutManager: VirtualLayoutManager? = null

    /**
     * 存放各个模块的适配器
     */
    private var mAdapters: MutableList<DelegateAdapter.Adapter<BaseViewHolder>>? = null
    private var delegateAdapter: DelegateAdapter? = null


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initVLayout()
    }


    private fun initVLayout() {
        mAdapters = LinkedList<DelegateAdapter.Adapter<BaseViewHolder>>()
        //初始化
        //创建VirtualLayoutManager对象
        layoutManager = VirtualLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        val viewPool = RecyclerView.RecycledViewPool()
        recyclerView.setRecycledViewPool(viewPool)
        viewPool.setMaxRecycledViews(0, 20)

        //设置适配器
        delegateAdapter = DelegateAdapter(layoutManager, true)
        recyclerView.setAdapter(delegateAdapter)

        //自定义各种不同适配器
        initAllTypeView()
        //设置适配器
        delegateAdapter!!.setAdapters(mAdapters as List<DelegateAdapter.Adapter<RecyclerView.ViewHolder>>?)
    }

    /**
     * 添加不同类型数据布局
     */
    private fun initAllTypeView() {
        initBannerView()
        initListFirstView()
        initRecyclerView()
        initFirstAdView()
        initListSecondView()
        initSecondAdView()
        initListThirdView()
        initListFourView()
        initListFiveView()
        initListSixView()
    }


    private fun initBannerView() {
        //banner
        val mAdapter =  object : BaseDelegateAdapter(
            this@LayoutActivity,
            LinearLayoutHelper(), R.layout.view_vlayout_banner,
            1, ViewType.typeBanner) {
           override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                // 绑定数据
                val mBanner = holder.getView(R.id.banner) as CustomBannerView
                mBanner.setHintGravity(1)
                mBanner.setAnimationDuration(1000)
                mBanner.setPlayDelay(2000)
                mBanner.setAdapter(CustomBannerAdapter1(this@LayoutActivity))
            }
        }
        mAdapters!!.add(mAdapter)
    }


    private fun initListFirstView() {
        initTitleView(1)
        val gridLayoutHelper = GridLayoutHelper(2)
        gridLayoutHelper.setPadding(0, 16, 0, 16)
        // 控制子元素之间的垂直间距
        gridLayoutHelper.setVGap(16)
        // 控制子元素之间的水平间距
        gridLayoutHelper.setHGap(0)
        gridLayoutHelper.setBgColor(Color.WHITE)
        val adapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            gridLayoutHelper,
            R.layout.view_vlayout_grid,
            4,
            ViewType.typeGv
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val ivImage =
                    holder.getView(R.id.iv_image) as ImageView
                ivImage.setBackgroundResource(R.drawable.icon_11)
                //String image = ConstantImage.homePageConcentration[position];
                //ImageUtil.loadImgByPicasso(LayoutActivity.this,ConstantImage.homePageConcentration[position],R.drawable.image_default,ivImage);
            }
        }
        mAdapters!!.add(adapter)
        initMoreView(1)
    }

    private fun initRecyclerView() {
        initTitleView(4)
        val adapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity, LinearLayoutHelper(),
            R.layout.view_vlayout_recycler, 1, ViewType.typeRecycler
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val recycler: RecyclerView = holder.getView(R.id.recyclerView) as RecyclerView
                recycler.setLayoutManager(
                    LinearLayoutManager(
                        this@LayoutActivity,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                )
                val narrowImageAdapter = NarrowImageAdapter(this@LayoutActivity)
                recycler.setAdapter(narrowImageAdapter)
                narrowImageAdapter.addAll(DataProvider.getNarrowImage(0))
            }
        }
        mAdapters!!.add(adapter)
    }


    private fun initFirstAdView() {
        initTitleView(6)
        val adAdapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            LinearLayoutHelper(),
            R.layout.view_vlayout_ad,
            1,
            ViewType.typeAd
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
            }
        }
        mAdapters!!.add(adAdapter)
    }


    private fun initListSecondView() {
        initTitleView(2)
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.setAspectRatio(4.0f)
        linearLayoutHelper.setDividerHeight(5)
        linearLayoutHelper.setMargin(0, 0, 0, 0)
        linearLayoutHelper.setPadding(0, 0, 0, 10)
        val adapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            linearLayoutHelper, R.layout.view_vlayout_news, 3, ViewType.typeList2
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
            }
        }
        mAdapters!!.add(adapter)
        initMoreView(2)
    }


    private fun initSecondAdView() {
        val adAdapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            LinearLayoutHelper(),
            R.layout.view_vlayout_ad,
            1,
            ViewType.typeAd2
        ) {
          override  fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                holder.getView(R.id.iv_image_ad)
                    .setBackgroundResource(R.drawable.icon_222)
            }
        }
        mAdapters!!.add(adAdapter)
    }


    private fun initListThirdView() {
        initTitleView(3)
        val gridLayoutHelper = GridLayoutHelper(2)
        gridLayoutHelper.setPadding(0, 16, 0, 16)
        // 控制子元素之间的垂直间距
        gridLayoutHelper.setVGap(16)
        // 控制子元素之间的水平间距
        gridLayoutHelper.setHGap(0)
        gridLayoutHelper.setBgColor(Color.WHITE)
        val adapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            gridLayoutHelper, R.layout.view_vlayout_grid, 2, ViewType.typeGv3
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val ivImage =
                    holder.getView(R.id.iv_image) as ImageView
                ivImage.setImageResource(R.drawable.icon_55)
            }
        }
        mAdapters!!.add(adapter)
        initMoreView(3)
    }


    private fun initListFourView() {
        initTitleView(4)
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.setAspectRatio(4.0f)
        linearLayoutHelper.setDividerHeight(5)
        linearLayoutHelper.setMargin(0, 0, 0, 0)
        linearLayoutHelper.setPadding(0, 0, 0, 10)
        val adapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            linearLayoutHelper, R.layout.view_vlayout_news, 3, ViewType.typeList4
        ) {
           override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
            }
        }
        mAdapters!!.add(adapter)
        initMoreView(4)
    }


    private fun initListFiveView() {
        initTitleView(5)
        val gridLayoutHelper = GridLayoutHelper(3)
        gridLayoutHelper.setPadding(0, 16, 0, 16)
        // 控制子元素之间的垂直间距
        gridLayoutHelper.setVGap(16)
        // 控制子元素之间的水平间距
        gridLayoutHelper.setHGap(0)
        gridLayoutHelper.setBgColor(Color.WHITE)
        val adapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            gridLayoutHelper, R.layout.view_vlayout_grid, 6, ViewType.typeGvBottom
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val ivImage =
                    holder.getView(R.id.iv_image) as ImageView
                ivImage.setImageResource(R.drawable.icon_88)
            }
        }
        mAdapters!!.add(adapter)
        initMoreView(4)
    }


    private fun initListSixView() {
        initTitleView(6)
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.setAspectRatio(4.0f)
        linearLayoutHelper.setDividerHeight(5)
        linearLayoutHelper.setMargin(0, 0, 0, 0)
        linearLayoutHelper.setPadding(0, 0, 0, 10)
        val adapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            linearLayoutHelper, R.layout.view_vlayout_news, 3, ViewType.typeList5
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
            }
        }
        mAdapters!!.add(adapter)
        initMoreView(6)
    }


    private fun initTitleView(type: Int) {
        val titleAdapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            LinearLayoutHelper(), R.layout.view_vlayout_title, 1, ViewType.typeTitle
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                when (type) {
                    1 -> holder.setText(R.id.tv_title, "标题")
                    2 -> holder.setText(R.id.tv_title, "标题")
                    3 -> holder.setText(R.id.tv_title, "标题")
                    4 -> holder.setText(R.id.tv_title, "标题")
                    5 -> holder.setText(R.id.tv_title, "标题")
                    6 -> holder.setText(R.id.tv_title, "标题")
                    else -> holder.setText(R.id.tv_title, "标题")
                }
                holder.getView(R.id.tv_change)
                    .setOnClickListener(View.OnClickListener {
                        when (type) {
                            1 -> showToast(this@LayoutActivity, "更多内容")
                            2 -> showToast(this@LayoutActivity, "更多内容")
                            3 -> showToast(this@LayoutActivity, "更多内容")
                            4 -> showToast(this@LayoutActivity, "更多内容")
                            5 -> showToast(this@LayoutActivity, "更多内容")
                            6 -> showToast(this@LayoutActivity, "更多内容")
                            else -> showToast(this@LayoutActivity, "更多内容")
                        }
                    })
            }
        }
        mAdapters!!.add(titleAdapter)
    }


    private fun initMoreView(type: Int) {
        val moreAdapter: BaseDelegateAdapter = object : BaseDelegateAdapter(
            this@LayoutActivity,
            LinearLayoutHelper(),
            R.layout.view_vlayout_more,
            1,
            ViewType.typeMore
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                when (type) {
                    1 -> holder.setText(R.id.tv_more, "查看更多")
                    2 -> holder.setText(R.id.tv_more, "查看更多")
                    3 -> holder.setText(R.id.tv_more, "查看更多")
                    4 -> holder.setText(R.id.tv_more, "查看更多")
                    5 -> holder.setText(R.id.tv_more, "查看更多")
                    6 -> holder.setText(R.id.tv_more, "没有更多数据")
                    else -> holder.setText(R.id.tv_more, "查看更多")
                }
                holder.getView(R.id.tv_more)
                    .setOnClickListener(View.OnClickListener {
                        when (type) {
                            1 -> showToast(this@LayoutActivity, "更多内容")
                            2 -> showToast(this@LayoutActivity, "更多内容")
                            3 -> showToast(this@LayoutActivity, "更多内容")
                            4 -> showToast(this@LayoutActivity, "更多内容")
                            5 -> showToast(this@LayoutActivity, "更多内容")
                            else -> showToast(this@LayoutActivity, "更多内容")
                        }
                    })
            }
        }
        mAdapters!!.add(moreAdapter)
    }


    private var toast: Toast? = null

    @SuppressLint("ShowToast")
    fun showToast(context: Context, content: String?) {
        if (toast == null) {
            toast = Toast.makeText(context.applicationContext, content, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(content)
        }
        toast!!.show()
    }

}