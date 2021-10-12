package com.ydl.list.views

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ydl.list.R

/**
 * @author yudongliang
 * create time 2021-10-12
 * describe : 带清除按钮的Edittext
 */
class ClearEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RegexEditText(context, attrs, defStyleAttr), View.OnTouchListener, View.OnFocusChangeListener,
    TextWatcher {

    private var mClearDrawable: Drawable? = null
    private var mTouchListener: OnTouchListener? = null
    private var mFocusChangeListener: OnFocusChangeListener? = null
    init {
        mClearDrawable = ContextCompat.getDrawable(context, R.drawable.input_delete_ic)?.let { DrawableCompat.wrap(it) }?.also {
            it.bounds = Rect(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        }
        onFocusChangeListener = this
        setDrawableVisible(false)
        setOnTouchListener(this)
        addTextChangedListener(this)
    }

    private fun setDrawableVisible(visible: Boolean){
        mClearDrawable?.let {
            if (it.isVisible == visible){
                return@let
            }
            it.setVisible(visible,false)
            setCompoundDrawablesRelative(
                compoundDrawablesRelative[0],
                compoundDrawablesRelative[1],
                if (visible)mClearDrawable else null,
                compoundDrawablesRelative[3])
        }
    }
    override fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener?) {
        mFocusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: OnTouchListener?) {
        mTouchListener = onTouchListener
    }
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val x = event.x.toInt()
        // 是否触摸了 Drawable
        var touchDrawable = false
        // 获取布局方向
        val layoutDirection = layoutDirection
        if (layoutDirection == LAYOUT_DIRECTION_LTR) {
            // 从左往右
            touchDrawable = x > width - mClearDrawable!!.intrinsicWidth - paddingEnd &&
                    x < width - paddingEnd
        } else if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            // 从右往左
            touchDrawable = x > paddingStart &&
                    x < paddingStart + mClearDrawable!!.intrinsicWidth
        }
        if (mClearDrawable!!.isVisible && touchDrawable) {
            if (event.action == MotionEvent.ACTION_UP) {
                setText("")
            }
            return true
        }
        return mTouchListener != null && mTouchListener!!.onTouch(view, event)
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus && text != null) {
            setDrawableVisible(text!!.isNotEmpty())
        } else {
            setDrawableVisible(false)
        }
        if (mFocusChangeListener != null) {
            mFocusChangeListener!!.onFocusChange(view, hasFocus)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun afterTextChanged(s: Editable) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
        if (isFocused) {
            setDrawableVisible(s.isNotEmpty())
        }
    }

}