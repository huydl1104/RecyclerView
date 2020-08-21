package com.ydl.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.ydl.list.activity.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        button10.setOnClickListener(this)
        button11.setOnClickListener(this)
        button12.setOnClickListener(this)
        button13.setOnClickListener(this)
        button14.setOnClickListener(this)
        button15.setOnClickListener(this)
        button16.setOnClickListener(this)
        button17.setOnClickListener(this)
        button18.setOnClickListener(this)
        button19.setOnClickListener(this)
        button20.setOnClickListener(this)
        button21.setOnClickListener(this)
        button22.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.button1->{startActivity(Intent(this, RefreshAndMoreActivity1::class.java)) }
            R.id.button2->{startActivity(Intent(this, RefreshAndMoreActivity2::class.java)) }
            R.id.button3->{startActivity(Intent(this, HeaderFooterActivity::class.java)) }
            R.id.button4->{startActivity(Intent(this, InsertRemoveActivity::class.java)) }
            R.id.button5->{startActivity(Intent(this, MultiStyleActivity::class.java)) }
            R.id.button6->{startActivity(Intent(this, StickyHeaderActivity::class.java)) }
            R.id.button7->{startActivity(Intent(this, StickyViewActivity::class.java)) }
            R.id.button8->{startActivity(Intent(this, StickyNormalActivity::class.java)) }
            R.id.button9->{startActivity(Intent(this, StaggeredGridActivity::class.java)) }
            R.id.button10->{startActivity(Intent(this, StaggeredActivity::class.java)) }
            R.id.button11->{startActivity(Intent(this, StaggeredLoadMoreActivity::class.java)) }
            R.id.button12->{startActivity(Intent(this, CollapsingActivity::class.java)) }
            R.id.button13->{startActivity(Intent(this, TouchMoveActivity::class.java)) }
            R.id.button14->{startActivity(Intent(this, TouchMoveActivity2::class.java)) }
            R.id.button15->{startActivity(Intent(this, DeleteAndTopActivity::class.java)) }
            R.id.button16->{startActivity(Intent(this, SpanRecyclerViewActivity::class.java)) }
            R.id.button17->{startActivity(Intent(this, TypeActivity::class.java)) }
            R.id.button18->{startActivity(Intent(this, LoadMoreActivity::class.java)) }
            R.id.button19->{startActivity(Intent(this, LayoutActivity::class.java)) }
            R.id.button20->{startActivity(Intent(this, HomeActivity::class.java)) }
            R.id.button21->{startActivity(Intent(this, TagRecyclerViewActivity::class.java)) }
            R.id.button22->{startActivity(Intent(this, ScrollActivity::class.java)) }
        }
    }
}
