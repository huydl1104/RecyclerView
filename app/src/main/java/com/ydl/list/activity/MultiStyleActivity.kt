package com.ydl.list.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydl.list.R
import com.ydl.list.adapter.PersonWithAdAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.utils.Utils
import com.ydl.listlib.itemline.DividerViewItemLine
import kotlinx.android.synthetic.main.activity_multi_style.*

class MultiStyleActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_style)

        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setProgressView(R.layout.view_progress)
        val itemDecoration = DividerViewItemLine(
            this.resources.getColor(R.color.color_f9f9f9)
            , Utils.dip2px(this, 0.5f),
            Utils.dip2px(this, 72f), 0
        )
//        recyclerView.addItemDecoration(itemDecoration)
        val adapter1 = PersonWithAdAdapter(this)
        val list = DataProvider.getPersonWithAds(0)
        list.forEach { Log.e("yuyu",""+it)}
        adapter1.addAll(list)
        recyclerView.setAdapterWithProgress(adapter1)

    }

}
