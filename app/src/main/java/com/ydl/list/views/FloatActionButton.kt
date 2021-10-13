package com.ydl.list.views

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author yudongliang
 * create time 2021-10-13
 * describe : 带悬浮动画的按钮
 */
class FloatActionButton : AppCompatImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) :super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun show() {
        removeCallbacks(hideRunnable)
        postDelayed(showRunnable, (ANIM_TIME * 2))
    }

    /**
     * 隐藏
     */
    fun hide() {
        removeCallbacks(showRunnable)
        post(hideRunnable)
    }
    private val ANIM_TIME = 300L

    private val showRunnable = Runnable {
        if (visibility != View.VISIBLE){
            visibility = View.VISIBLE
        }
        val showAnimator =  ValueAnimator.ofFloat(0f,1f).also {
            it.duration = ANIM_TIME
            it.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                alpha = value
                scaleX = value
                scaleY = value
            }
        }
        showAnimator.start()

    }

    private val hideRunnable = Runnable {
        val hideAnimator =  ValueAnimator.ofFloat(1f,0f).also {
            it.duration = ANIM_TIME
            it.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                alpha = value
                scaleX = value
                scaleY = value
                if (value == 0f) {
                    visibility = INVISIBLE
                }
            }
        }
        hideAnimator.start()
    }

}