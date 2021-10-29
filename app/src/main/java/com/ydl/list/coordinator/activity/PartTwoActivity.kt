package com.ydl.list.coordinator.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ydl.list.R
import com.ydl.list.utils.Utils
import kotlinx.android.synthetic.main.activity_part_two.*
import java.util.ArrayList

class PartTwoActivity : AppCompatActivity() {

    var mToolbarHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_two)
        mToolbarHeight = Utils.getToolbarHeight(this@PartTwoActivity)
        toolbar.let {
            setSupportActionBar(it)
            title = "PartTwo"
            it.setTitleTextColor(ActivityCompat.getColor(this,android.R.color.white))
        }
        val paddingTop = Utils.getToolbarHeight(this@PartTwoActivity) + Utils.getTabsHeight(this@PartTwoActivity)
        recyclerView.also {
            it.setPadding(recyclerView.paddingLeft, paddingTop, recyclerView.paddingRight, recyclerView.paddingBottom)
            it.layoutManager = LinearLayoutManager(this@PartTwoActivity)
            it.adapter = RecyclerTwoAdapter(createItemList())
            it.addOnScrollListener(object :HidingTwoScrollListener(this@PartTwoActivity){
                override fun onMoved(distance: Int) {
                    toolbarContainer.translationY = (-distance).toFloat()
                }

                override fun onShow() {
                    toolbarContainer.animate().translationY(0f)
                        .setInterpolator(DecelerateInterpolator(2f)).start()
                }

                override fun onHide() {
                    toolbarContainer.animate().translationY(-mToolbarHeight.toFloat())
                        .setInterpolator(
                            AccelerateInterpolator(2f)
                        ).start()
                }

            })
        }


    }

    private fun createItemList(): List<String> {
        val itemList: MutableList<String> = ArrayList()
        for (i in 0..19) {
            itemList.add("Item $i")
        }
        return itemList
    }

}


abstract class HidingTwoScrollListener(context: Context) : RecyclerView.OnScrollListener() {
    private var mToolbarOffset = 0
    private var mControlsVisible = true
    private val mToolbarHeight = Utils.getToolbarHeight(context)
    private var mTotalScrolledDistance = 0
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mTotalScrolledDistance < mToolbarHeight) {
                setVisible()
            } else {
                if (mControlsVisible) {
                    if (mToolbarOffset > HIDE_THRESHOLD) {
                        setInvisible()
                    } else {
                        setVisible()
                    }
                } else {
                    if (mToolbarHeight - mToolbarOffset > SHOW_THRESHOLD) {
                        setVisible()
                    } else {
                        setInvisible()
                    }
                }
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        clipToolbarOffset()
        onMoved(mToolbarOffset)
        if (mToolbarOffset < mToolbarHeight && dy > 0 || mToolbarOffset > 0 && dy < 0) {
            mToolbarOffset += dy
        }
        if (mTotalScrolledDistance < 0) {
            mTotalScrolledDistance = 0
        } else {
            mTotalScrolledDistance += dy
        }
    }

    private fun clipToolbarOffset() {
        if (mToolbarOffset > mToolbarHeight) {
            mToolbarOffset = mToolbarHeight
        } else if (mToolbarOffset < 0) {
            mToolbarOffset = 0
        }
    }

    private fun setVisible() {
        if (mToolbarOffset > 0) {
            onShow()
            mToolbarOffset = 0
        }
        mControlsVisible = true
    }

    private fun setInvisible() {
        if (mToolbarOffset < mToolbarHeight) {
            onHide()
            mToolbarOffset = mToolbarHeight
        }
        mControlsVisible = false
    }

    abstract fun onMoved(distance: Int)
    abstract fun onShow()
    abstract fun onHide()

    companion object {
        private const val HIDE_THRESHOLD = 10f
        private const val SHOW_THRESHOLD = 70f
    }

}

class RecyclerItemTwoViewHolder(parent: View, private val mItemTextView: TextView) : RecyclerView.ViewHolder(parent) {
    fun setItemText(text: CharSequence) {
        mItemTextView.text = text
    }

    companion object {
        fun newInstance(parent: View): RecyclerItemTwoViewHolder {
            val itemTextView = parent.findViewById<View>(R.id.itemTextView) as TextView
            return RecyclerItemTwoViewHolder(parent, itemTextView)
        }
    }
}

class RecyclerTwoAdapter(private val mItemList: List<String>) : RecyclerView.Adapter<RecyclerItemTwoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemTwoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return RecyclerItemTwoViewHolder.newInstance(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerItemTwoViewHolder ,position: Int) {
        viewHolder.setItemText( mItemList[position])
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

}