package com.ydl.list.activity

import android.os.Bundle
import android.os.Handler
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydl.list.R
import com.ydl.list.adapter.HomePageAdapter
import com.ydl.list.data.HomePageEntry
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*

class HomeActivity:AppCompatActivity() {

    private var adapter: HomePageAdapter? = null
    private var handler: Handler? = Handler()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initRecyclerView()
        initData()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
    }

    private fun initRecyclerView() {


//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
//        {
//            @Override
//            public int getSpanSize(int position)
//            {
//                int viewType = getItemViewType(position);
//                if (mHeaderViews.get(viewType) != null)
//                {
//                    return layoutManager.getSpanCount();
//                } else if (mFootViews.get(viewType) != null)
//                {
//                    return layoutManager.getSpanCount();
//                }
//                if (spanSizeLookup != null)
//                    return spanSizeLookup.getSpanSize(position);
//                return 1;
//            }
//        });
//        gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());


        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        adapter = HomePageAdapter(this)
        recyclerView.setAdapter(adapter)
    }


    private fun initData() {
        handler!!.postDelayed({
            val list: ArrayList<HomePageEntry> = ArrayList<HomePageEntry>()
            //第一条是banner
            list.add(HomePageEntry(0))
            //第二组
            list.add(HomePageEntry(3))
            list.add(HomePageEntry(4))
            list.add(HomePageEntry(4))
            list.add(HomePageEntry(4))
            //第三组
            list.add(HomePageEntry(1))
            //第四组
            list.add(HomePageEntry(3))
            list.add(HomePageEntry(2))
            list.add(HomePageEntry(2))
            list.add(HomePageEntry(2))
            list.add(HomePageEntry(2))
            list.add(HomePageEntry(2))
            list.add(HomePageEntry(2))
            //第五组
            //第六组
            list.add(HomePageEntry(3))
            list.add(HomePageEntry(4))
            list.add(HomePageEntry(4))
            list.add(HomePageEntry(4))
            //第七组
            list.add(HomePageEntry(3))
            list.add(HomePageEntry(2))
            list.add(HomePageEntry(2))
            list.add(HomePageEntry(2))
            list.add(HomePageEntry(2))
            //第八组
            //第九组
            list.add(HomePageEntry(3))
            list.add(HomePageEntry(4))
            list.add(HomePageEntry(4))
            list.add(HomePageEntry(4))
            adapter!!.setData(list)
        }, 50)
    }


}