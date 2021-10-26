package com.ydl.list.coordinator.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

import com.example.base.FragmentPagerAdapter
import com.ydl.list.R
import com.ydl.list.ui.fragments.TestCoorFragment
import kotlinx.android.synthetic.main.activity_coordinator.*


class CoordinatorSimpleActivity : AppCompatActivity() {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, CoordinatorSimpleActivity::class.java))
        }
    }

    private var mImageArray = intArrayOf(
        R.mipmap.bg_android,
        R.mipmap.bg_ios,
        R.mipmap.bg_js,
        R.mipmap.bg_other
    )
    private  var mColorArray = intArrayOf(
        android.R.color.holo_blue_light,
        android.R.color.holo_red_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_green_light
    )
    private var mFragments: ArrayList<Fragment>? = null
    private val mTitles = arrayOf("Android", "iOS", "Web", "Other")
    private var mViewPager: ViewPager? = null
    private var mPagerAdapter: FragmentPagerAdapter<Fragment>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)
        initFragments()
        initViewPager()

        coordinatortablayout!!.setTranslucentStatusBar(this)
            .setTitle("Demo")
            .setBackEnable(true)
            .setImageArray(mImageArray, mColorArray)
            .setupWithViewPager(mViewPager)
            .setLeftClickListener { finish() }
    }

    private fun initFragments() {
        mFragments = ArrayList()
        for (title in mTitles) {
            mFragments!!.add(TestCoorFragment.getInstance(title))
        }
    }

    private fun initViewPager() {
        mViewPager = findViewById<View>(R.id.vp) as ViewPager
        mViewPager!!.offscreenPageLimit = 4
        mPagerAdapter = FragmentPagerAdapter(this)
        mPagerAdapter!!.addFragment(TestCoorFragment.getInstance("1"),mTitles[0])
        mPagerAdapter!!.addFragment(TestCoorFragment.getInstance("2"),mTitles[1])
        mPagerAdapter!!.addFragment(TestCoorFragment.getInstance("3"),mTitles[2])
        mPagerAdapter!!.addFragment(TestCoorFragment.getInstance("4"),mTitles[3])
        mViewPager!!.adapter = mPagerAdapter
    }

}