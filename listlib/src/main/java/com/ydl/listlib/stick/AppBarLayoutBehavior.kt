package com.ydl.listlib.stick

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import java.lang.reflect.Field

class AppBarLayoutBehavior(
    context: Context?,
    attrs: AttributeSet?
) : AppBarLayout.Behavior(context, attrs) {
    /**
     * 记录是否有fling
     */
    private var isFlinging = false
    /**
     * 记录是否
     */
    private var shouldBlockNestedScroll = false

    /**
     * 是否拦截触摸事件
     */
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        shouldBlockNestedScroll = isFlinging
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN ->  //手指触摸屏幕的时候停止fling事件
                stopAppbarLayoutFling(child)
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }

    /**
     * 反射获取私有的flingRunnable 属性，考虑support 28以后变量名修改的问题
     */
    private fun getFlingRunnableField(): Field?{
        val superclass: Class<*>? = this.javaClass.superclass
        return try { // support design 27及一下版本
            var headerBehaviorType: Class<*>? = null
            if (superclass != null) {
                val name = superclass.name
                headerBehaviorType = superclass.superclass
            }
            if (headerBehaviorType != null) {
                val name = headerBehaviorType.name
                headerBehaviorType.getDeclaredField("mFlingRunnable")
            } else {
                null
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            // 可能是28及以上版本
            val headerBehaviorType = superclass!!.superclass!!.superclass
            if (headerBehaviorType != null) {
                val name = headerBehaviorType.name
                headerBehaviorType.getDeclaredField("flingRunnable")
            } else {
                null
            }
        }
    }

    /**
     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
     */
    private fun getScrollerField(): Field?{
        val superclass: Class<*>? = this.javaClass.superclass
        return try { // support design 27及一下版本
            var headerBehaviorType: Class<*>? = null
            if (superclass != null) {
                headerBehaviorType = superclass.superclass
            }
            headerBehaviorType?.getDeclaredField("mScroller")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            // 可能是28及以上版本
            val headerBehaviorType = superclass!!.superclass!!.superclass
            headerBehaviorType?.getDeclaredField("scroller")
        }
    }

    /**
     * 停止appbarLayout的fling事件
     * @param appBarLayout
     */
    private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) { //通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            val flingRunnableField = getFlingRunnableField()
            val scrollerField = getScrollerField()
            if (flingRunnableField != null) {
                flingRunnableField.isAccessible = true
            }
            if (scrollerField != null) {
                scrollerField.isAccessible = true
            }
            var flingRunnable: Runnable? = null
            if (flingRunnableField != null) {
                val field = flingRunnableField[this];
                if (field != null){
                    flingRunnable = field as Runnable
                }
            }
            var overScroller: OverScroller? = null
            if (scrollerField != null) {
                val scrollerFiled= scrollerField[this]
                if (scrollerFiled != null){
                    overScroller = scrollerFiled as OverScroller
                }
            }
            //下面是关键点
            if (flingRunnable != null) {
                LogUtil.d(TAG, "存在flingRunnable")
                appBarLayout.removeCallbacks(flingRunnable)
                flingRunnableField!![this] = null
            }
            if (overScroller != null && !overScroller.isFinished) {
                overScroller.abortAnimation()
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * 嵌套滑动开始（ACTION_DOWN），确定Behavior是否要监听此次事件
     */
    override fun onStartNestedScroll(
        parent: CoordinatorLayout, child: AppBarLayout,
        directTargetChild: View, target: View,
        nestedScrollAxes: Int, type: Int
    ): Boolean {
        LogUtil.d(TAG, "onStartNestedScroll")
        stopAppbarLayoutFling(child)
        return super.onStartNestedScroll(
            parent, child, directTargetChild, target,
            nestedScrollAxes, type
        )
    }

    /**
     * 嵌套滑动进行中，要监听的子 View将要滑动，滑动事件即将被消费（但最终被谁消费，可以通过代码控制）
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout, target: View,
        dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        LogUtil.d(TAG, "onNestedPreScroll:" + child.totalScrollRange + " ,dx:" + dx + " ,dy:" + dy + " ,type:" + type)
        //type返回1时，表示当前target处于非touch的滑动，
        //该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动
        //子类还未结束其自身的fling
        //所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        if (type == TYPE_FLING) {
            isFlinging = true
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }
    }

    /**
     * 嵌套滑动进行中，要监听的子 View的滑动事件已经被消费
     */
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        LogUtil.d(TAG, "onNestedScroll: target:" + target.javaClass + " ,"
                    + child.totalScrollRange + " ,dxConsumed:"
                    + dxConsumed + " ,dyConsumed:" + dyConsumed + " " + ",type:" + type)
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(
                coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type
            )
        }
    }

    /**
     * 嵌套滑动结束（ACTION_UP或ACTION_CANCEL）
     */
    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout, abl: AppBarLayout,
        target: View, type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
        isFlinging = false
        shouldBlockNestedScroll = false
    }

    private object LogUtil {
        fun d(tag: String, string: String) {
            Log.d(tag, string)
        }
    }

    companion object {
        private const val TAG = "AppbarLayoutBehavior"
        private const val TYPE_FLING = 1
    }
}