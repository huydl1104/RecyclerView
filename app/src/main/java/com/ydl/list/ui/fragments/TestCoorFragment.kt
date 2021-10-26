package com.ydl.list.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.R
import com.ydl.list.adapter.RecyclerAdapter


/**
 * @author yudongliang
 * create time 2021-10-18
 * describe :
 */
class TestCoorFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null

    private var mDatas: MutableList<String>? = null

    private var mTitle: String? = null
    companion object{
        private val ARG_TITLE = "title"
        fun getInstance(title: String): TestCoorFragment {
            val fra = TestCoorFragment()
            val bundle = Bundle()
            bundle.putString(ARG_TITLE, title)
            fra.arguments = bundle
            return fra
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = arguments
        mTitle = bundle!!.getString(ARG_TITLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_main, container, false)
        initData()
        mRecyclerView = v.findViewById(R.id.recyclerview)
        mRecyclerView!!.layoutManager = LinearLayoutManager(mRecyclerView!!.context)
        mRecyclerView!!.adapter = RecyclerAdapter(mRecyclerView!!.context, mDatas)
        return v
    }

    private fun initData() {
        mDatas = ArrayList()
        var i = 'A'.toInt()
        while (i < 'z'.toInt()) {
            mDatas!!.add(mTitle + i.toChar())
            i++
        }
    }
}