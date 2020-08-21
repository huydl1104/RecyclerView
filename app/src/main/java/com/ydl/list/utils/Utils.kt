package com.ydl.list.utils

import android.app.Activity
import android.content.Context

object Utils {
    fun dip2px(ctx: Context, dpValue: Float): Int {
        val scale = ctx.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi / 160f)).toInt()
    }


    fun getScreenWidth(activity: Activity): Int {
        var width = 0
        val windowManager = activity.windowManager
        val display = windowManager.defaultDisplay
        width = display.width
        return width
    }
}