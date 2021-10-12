package com.ydl.list.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author yudongliang
 * create time 2021-10-12
 * describe : 长按半透明松手恢复的TextView
 */
class PressAlphaTextView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr){

    override fun dispatchSetPressed(pressed: Boolean) {
        super.dispatchSetPressed(pressed)
        alpha = if(pressed){ 0.5f }else{ 1.0f }
    }

}