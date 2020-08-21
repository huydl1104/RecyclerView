package com.ydl.list.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydl.list.R
import com.ydl.list.adapter.PersonAdapter
import com.ydl.list.data.DataProvider
import com.ydl.list.data.PersonData
import com.ydl.list.utils.Utils
import com.ydl.listlib.itemline.DividerViewItemLine
import kotlinx.android.synthetic.main.activity_insert_remove.*
import java.util.*

class InsertRemoveActivity :AppCompatActivity() {
    private var mAdapter :PersonAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_remove)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(linearLayoutManager)
        val itemDecoration = DividerViewItemLine(
            this.resources.getColor(R.color.color_f9f9f9)
            , Utils.dip2px(this, 1f),
            Utils.dip2px(this, 72f), 0
        )
        itemDecoration.setDrawLastItem(false)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.setAdapterWithProgress(PersonAdapter(this).also { mAdapter = it })


        val persons = DataProvider.getPersonList(0)
        mAdapter!!.addAll(persons.subList(0, 3))
        val defaultItemAnimator = DefaultItemAnimator()
        recyclerView.setItemAnimator(defaultItemAnimator)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_insert, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val random = Random()
        val len: Int = mAdapter!!.middleCount
        if (len > 0) {
            val pos = random.nextInt(len)
            val persons: List<PersonData> = DataProvider.getPersonList(0)
            val data: PersonData = persons[random.nextInt(persons.size)]
            when (item.itemId) {
                R.id.ic_add -> mAdapter!!.insert(data, pos)
                R.id.ic_remove -> mAdapter!!.remove(pos)
                R.id.ic_refresh -> mAdapter!!.update(data, pos)
                else -> {
                }
            }
        }
        return true
    }
}