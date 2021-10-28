package com.ydl.list.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import com.ydl.list.R
import com.ydl.list.views.BlurringView
import java.util.*

class BlurViewActivity : AppCompatActivity() {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, BlurViewActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blur_view)
        mBlurringView = findViewById(R.id.blurring_view)
        val blurredView: View = findViewById(R.id.blurred_view)

        // Give the blurring view a reference to the blurred view.
        mBlurringView!!.setBlurredView(blurredView)
        mImageViews[0] = findViewById(R.id.image0)
        mImageViews[1] = findViewById(R.id.image1)
        mImageViews[2] = findViewById(R.id.image2)
        mImageViews[3] = findViewById(R.id.image3)
        mImageViews[4] = findViewById(R.id.image4)
        mImageViews[5] = findViewById(R.id.image5)
        mImageViews[6] = findViewById(R.id.image6)
        mImageViews[7] = findViewById(R.id.image7)
        mImageViews[8] = findViewById(R.id.image8)
    }

    fun shuffle(view: View?) {

        // Randomly pick a different start in the array of available images.
        var newStartIndex: Int
        do {
            newStartIndex = IMAGE_IDS[mRandom.nextInt(IMAGE_IDS.size)]
        } while (newStartIndex == mStartIndex)
        mStartIndex = newStartIndex

        // Update the images for the image views contained in the blurred view.
        for (i in mImageViews.indices) {
            val drawableId = IMAGE_IDS[(mStartIndex + i) % IMAGE_IDS.size]
            mImageViews[i]!!.setImageDrawable(resources.getDrawable(drawableId))
        }

        // Invalidates the blurring view when the content of the blurred view changes.
        mBlurringView?.invalidate()
    }

    private val listener = ValueAnimator.AnimatorUpdateListener { mBlurringView?.invalidate() }

    fun shift(view: View?) {
        if (!mShifted) {
            for (imageView in mImageViews) {
                val tx = ObjectAnimator.ofFloat(
                    imageView,
                    View.TRANSLATION_X,
                    (mRandom.nextFloat() - 0.5f) * 500
                )
                tx.addUpdateListener(listener)
                val ty = ObjectAnimator.ofFloat(
                    imageView,
                    View.TRANSLATION_Y,
                    (mRandom.nextFloat() - 0.5f) * 500
                )
                ty.addUpdateListener(listener)
                val set = AnimatorSet()
                set.playTogether(tx, ty)
                set.duration = 3000
                set.interpolator = OvershootInterpolator()
                set.addListener(AnimationEndListener(imageView))
                set.start()
            }
            mShifted = true
        } else {
            for (imageView in mImageViews) {
                val tx = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0f)
                tx.addUpdateListener(listener)
                val ty = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, 0f)
                ty.addUpdateListener(listener)
                val set = AnimatorSet()
                set.playTogether(tx, ty)
                set.duration = 3000
                set.interpolator = OvershootInterpolator()
                set.addListener(AnimationEndListener(imageView))
                set.start()
            }
            mShifted = false
        }
    }

    private var mBlurringView: BlurringView? = null

    private val IMAGE_IDS = intArrayOf(
        R.drawable.p0, R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
        R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9
    )

    private val mImageViews = arrayOfNulls<ImageView>(9)
    private var mStartIndex = 0

    private val mRandom = Random()

    private var mShifted = false

    private class AnimationEndListener(var mView: View?) : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            mView!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }

        override fun onAnimationEnd(animation: Animator) {
            mView!!.setLayerType(View.LAYER_TYPE_NONE, null)
        }

        override fun onAnimationCancel(animation: Animator) {
            mView!!.setLayerType(View.LAYER_TYPE_NONE, null)
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }
}