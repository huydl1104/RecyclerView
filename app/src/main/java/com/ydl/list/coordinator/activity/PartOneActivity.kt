package com.ydl.list.coordinator.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.R
import kotlinx.android.synthetic.main.activity_part_one.*
import java.lang.RuntimeException
import java.util.ArrayList

class PartOneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_one)

        toolbar.let {
            setSupportActionBar(it)
            title = "PartOne"
            it.setTitleTextColor(ActivityCompat.getColor(this,android.R.color.white))
        }
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this@PartOneActivity)
            it.adapter = RecyclerAdapter(createItemList())
            it.addOnScrollListener(object :HidingScrollListener() {
                override fun onHide() {
                    hideViews()
                }

                override fun onShow() {
                    showViews()
                }

            })
        }

    }

    private fun hideViews() {
        toolbar.animate().translationY((-toolbar.height).toFloat()).interpolator = AccelerateInterpolator(2F)
        val lp = fabButton.layoutParams as FrameLayout.LayoutParams
        val fabBottomMargin = lp.bottomMargin
        fabButton.animate()
            .translationY((fabButton.height + fabBottomMargin).toFloat())
            .setInterpolator(AccelerateInterpolator(2F)).start()
    }

    private fun showViews() {
        toolbar.animate().translationY(0f).interpolator = DecelerateInterpolator(2f)
        fabButton.animate().translationY(0f).setInterpolator(DecelerateInterpolator(2f)).start()
    }

    private fun createItemList(): List<String> {
        val itemList: MutableList<String> = ArrayList()
        for (i in 0..19) {
            itemList.add("Item $i")
        }
        return itemList
    }

}



class RecyclerHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class RecyclerItemViewHolder(parent: View?, private val mItemTextView: TextView) :
    RecyclerView.ViewHolder(parent!!) {
    fun setItemText(text: CharSequence) {
        mItemTextView.text = text
    }

    companion object {
        fun newInstance(parent: View): RecyclerItemViewHolder {
            val itemTextView = parent.findViewById<View>(R.id.itemTextView) as TextView
            return RecyclerItemViewHolder(parent, itemTextView)
        }
    }
}

class RecyclerAdapter(private val mItemList: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        if (viewType == TYPE_ITEM) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
            return RecyclerItemViewHolder.newInstance(view)
        } else if (viewType == TYPE_HEADER) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false)
            return RecyclerHeaderViewHolder(view)
        }
        throw RuntimeException("There is no type that matches the type $viewType + make sure your using types correctly")
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (!isPositionHeader(position)) {
            val holder: RecyclerItemViewHolder = viewHolder as RecyclerItemViewHolder
            val itemText = mItemList[position - 1] // header
            holder.setItemText(itemText)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) {
            TYPE_HEADER } else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    companion object {
        private const val TYPE_HEADER = 2
        private const val TYPE_ITEM = 1
    }

    override fun getItemCount(): Int {
        return mItemList.size + 1 // header
    }
}

abstract class HidingScrollListener : RecyclerView.OnScrollListener() {
    private var mScrolledDistance = 0
    private var mControlsVisible = true
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        if (firstVisibleItem == 0) {
            if (!mControlsVisible) {
                onShow()
                mControlsVisible = true
            }
        } else {
            if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                onHide()
                mControlsVisible = false
                mScrolledDistance = 0
            } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                onShow()
                mControlsVisible = true
                mScrolledDistance = 0
            }
        }
        if (mControlsVisible && dy > 0 || !mControlsVisible && dy < 0) {
            mScrolledDistance += dy
        }
    }

    abstract fun onHide()
    abstract fun onShow()

    companion object {
        private const val HIDE_THRESHOLD = 20
    }
}



