package com.ydl.list.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ydl.list.R

/**
 * @author yudongliang
 * create time 2021-10-13
 * describe : 可以设置drawable的大小的
 */
class DrawableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mDrawableWidth = 0
    private var mDrawableHeight = 0

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)
        mDrawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableWidth, 0)
        mDrawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableHeight, 0)
        array.recycle()
        //更新 drawable size
        refreshDrawableSize()
    }

    /** 从外部设置 drawable size */
    fun setDrawableSize(drawableWidth: Int,drawableHeight: Int){
        this.mDrawableWidth = drawableWidth
        this.mDrawableHeight = drawableHeight
        if (!isAttachedToWindow){
            return
        }
        refreshDrawableSize()
    }

    private fun refreshDrawableSize(){
        if (mDrawableWidth == 0 || mDrawableHeight == 0 ){
            return
        }
//        if (compoundDrawables.isNotEmpty())


    }

}