package com.ydl.listlib.swipe

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.ydl.listlib.R

class CustomSwipeMenu  : ViewGroup {

    private var isTouching = false //防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空
    private var mScaleTouchSlop = 0 //处理单击事件的冲突
    private var mMaxVelocity = 0 //计算滑动速度用
    private var mPointerId = 0 //多点触摸只算第一根手指的速度
    private var mRightMenuWidths = 0//右侧菜单宽度总和(最大滑动距离)
    private var mLimit = 0//滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu
    private var mContentView: View? = null//存储contentView(第一个View)
    //private Scroller mScroller;//以前item的滑动动画靠它做，现在用属性动画做
    private val mLastP = PointF()//上一次的xy
    //dispatch函数里，每次down时，为true，move时判断，如果是滑动动作，设为false。
    //在Intercept函数的up时，判断这个变量，如果仍为true 说明是点击事件，则关闭菜单。
    private var isUnMoved = true
    //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
    //up-down的坐标，判断是否是滑动，如果是，则屏蔽一切点击事件
    private val mFirstP = PointF()
    private var isUserSwiped = false

    private var mVelocityTracker: VelocityTracker? = null//滑动速度变量
    private var isSwipeEnable = false//设置侧滑功能开关,默认开
    private var isIos = false//IOS的交互方式
    private var iosInterceptFlag = false //IOS类型下，是否拦截事件的flag
    private var isLeftSwipe = false  //左滑右滑的开关,默认左滑打开菜单
    //平滑展开和关闭的动画效果
    private var mExpandAnim: ValueAnimator? = null
    private var mCloseAnim: ValueAnimator? = null
    private var isExpand =false

    companion object{
        //存储的是当前正在展开的View
        var mViewCache: CustomSwipeMenu? = null
    }

    constructor(context:Context):this(context,null)
    constructor(context:Context, attrs: AttributeSet? = null):this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):super(context,attrs,defStyleAttr){
        mScaleTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mMaxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        //初始化滑动帮助类对象
        //mScroller = new Scroller(context);
        isSwipeEnable = true //右滑删除功能的开关,默认开
        isIos = true//IOS、QQ式交互，默认开
        isLeftSwipe = true//左滑右滑的开关,默认左滑打开菜单
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomSwipeMenu, defStyleAttr, 0)
        val count = ta.indexCount
        for (i in 0 until count) {
            when (val attr = ta.getIndex(i)) {
                R.styleable.CustomSwipeMenu_swipeEnable -> {
                    isSwipeEnable = ta.getBoolean(attr, true)
                }
                R.styleable.CustomSwipeMenu_ios -> {
                    isIos = ta.getBoolean(attr, true)
                }
                R.styleable.CustomSwipeMenu_leftSwipe -> {
                    isLeftSwipe = ta.getBoolean(attr, true)
                }
            }
        }
        ta.recycle()
    }



    /**
     * 设置是否开启IOS阻塞式交互
     */
    fun setIos(ios: Boolean): CustomSwipeMenu {
        isIos = ios
        return this
    }

    /**
     * 设置是否开启左滑出菜单，设置false 为右滑出菜单
     */
    fun setLeftSwipe(leftSwipe: Boolean): CustomSwipeMenu {
        isLeftSwipe = leftSwipe
        return this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        isClickable = true //令自己可点击，从而获取触摸事件
        mRightMenuWidths = 0 //由于ViewHolder的复用机制，每次这里要手动恢复初始值
        var mHeight = 0
        var contentWidth = 0 //2016 11 09 add,适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度
        val innerChildCount = childCount
        //add by 2016 08 11 为了子View的高，可以matchParent(参考的FrameLayout 和LinearLayout的Horizontal)
        val measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY
        var isNeedMeasureChildHeight = false
        for (i in 0 until innerChildCount) {
            val childView = getChildAt(i)
            //令每一个子View可点击，从而获取触摸事件
            childView.isClickable = true
            if (childView.visibility != View.GONE) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
                //measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                val lp = childView.layoutParams as MarginLayoutParams
                mHeight = Math.max(mHeight, childView.measuredHeight /* + lp.topMargin + lp.bottomMargin*/)
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true
                }
                if (i > 0) { //第一个布局是Left item，从第二个开始才是RightMenu
                    mRightMenuWidths += childView.measuredWidth
                } else {
                    mContentView = childView
                    contentWidth = childView.measuredWidth
                }
            }
        }
        //宽度取第一个Item(Content)的宽度
        setMeasuredDimension(
            paddingLeft + paddingRight + contentWidth,
            mHeight + paddingTop + paddingBottom
        )
        mLimit = mRightMenuWidths * 4 / 10 //滑动判断的临界值
        //如果子View的height有MatchParent属性的，设置子View高度
        if (isNeedMeasureChildHeight) {
            forceUniformHeight(innerChildCount, widthMeasureSpec)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * 给MatchParent的子View设置高度
     */
    private fun forceUniformHeight(
        count: Int,
        widthMeasureSpec: Int
    ) {
        val uniformMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY) //以父布局高度构建一个Exactly的测量参数
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val lp = child.layoutParams as MarginLayoutParams
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    val oldWidth = lp.width
                    lp.width = child.measuredWidth
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0)
                    lp.width = oldWidth
                }
            }
        }
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        val innerChildCount = childCount
        var left = 0 + paddingLeft
        var right = 0 + paddingLeft
        for (i in 0 until innerChildCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE) {
                if (i == 0) { //第一个子View是内容 宽度设置为全屏
                    childView.layout(
                        left,
                        paddingTop,
                        left + childView.measuredWidth,
                        paddingTop + childView.measuredHeight
                    )
                    left = left + childView.measuredWidth
                } else {
                    if (isLeftSwipe) {
                        childView.layout(
                            left,
                            paddingTop,
                            left + childView.measuredWidth,
                            paddingTop + childView.measuredHeight
                        )
                        left = left + childView.measuredWidth
                    } else {
                        childView.layout(
                            right - childView.measuredWidth,
                            paddingTop,
                            right,
                            paddingTop + childView.measuredHeight
                        )
                        right = right - childView.measuredWidth
                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isSwipeEnable) {
            acquireVelocityTracker(ev)
            val verTracker = mVelocityTracker
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    isUserSwiped = false //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    isUnMoved = true //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                    iosInterceptFlag = false //add by 2016 09 11 ，每次DOWN时，默认是不拦截的
//                    isTouching = if (isTouching) { //如果有别的指头摸过了，那么就return false。这样后续的move..等事件也不会再来找这个View了。
//                            return false
//                        } else {
//                            true //第一个摸的指头，赶紧改变标志，
//                        }

                    mLastP[ev.rawX] = ev.rawY
                    mFirstP[ev.rawX] = ev.rawY //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    //如果down，view和cacheview不一样，则立马让它还原。且把它置为null
                    Log.e("yu--","mViewCache  -->$mViewCache , mazu -> ${(mViewCache !== this)} , ev->${ev.action}")
                    if (mViewCache != null) {
                        if (mViewCache !== this) {
                            mViewCache!!.smoothClose()
                            iosInterceptFlag = isIos //IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件。
                        }
                        //只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                    //求第一个触点的id， 此时可能有多个触点，但至少一个，计算滑动速率用
                    mPointerId = ev.getPointerId(0)
                }
                MotionEvent.ACTION_MOVE -> {
                    //IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!iosInterceptFlag) {
                        val gap = mLastP.x - ev.rawX
                        //为了在水平滑动中禁止父类ListView等再竖直滑动
                        if (Math.abs(gap) > 10 || Math.abs(scrollX) > 10) { //2016 09 29 修改此处，使屏蔽父布局滑动更加灵敏，
                            parent.requestDisallowInterceptTouchEvent(true)
                        }
                        //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。begin
                        if (Math.abs(gap) > mScaleTouchSlop) {
                            isUnMoved = false
                        }
                        //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。end
                    //如果scroller还没有滑动结束 停止滑动动画
                    /* if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }*/scrollBy(gap.toInt(), 0) //滑动使用scrollBy
                        //越界修正
                        if (isLeftSwipe) { //左滑
                            if (scrollX < 0) {
                                scrollTo(0, 0)
                            }
                            if (scrollX > mRightMenuWidths) {
                                scrollTo(mRightMenuWidths, 0)
                            }
                        } else { //右滑
                            if (scrollX < -mRightMenuWidths) {
                                scrollTo(-mRightMenuWidths, 0)
                            }
                            if (scrollX > 0) {
                                scrollTo(0, 0)
                            }
                        }
                        mLastP[ev.rawX] = ev.rawY
                    }

                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (Math.abs(ev.rawX - mFirstP.x) > mScaleTouchSlop) {
                        isUserSwiped = true
                    }
                    //IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!iosInterceptFlag) { //且滑动了 才判断是否要收起、展开menu
                        //求伪瞬时速度
                        verTracker!!.computeCurrentVelocity(1000, mMaxVelocity.toFloat())
                        val velocityX = verTracker.getXVelocity(mPointerId)
                        if (Math.abs(velocityX) > 1000) { //滑动速度超过阈值
                            if (velocityX < -1000) {
                                if (isLeftSwipe) { //左滑
                                    //平滑展开Menu
                                    smoothExpand()
                                } else { //平滑关闭Menu
                                    smoothClose()
                                }
                            } else {
                                if (isLeftSwipe) { //左滑
                                    // 平滑关闭Menu
                                    smoothClose()
                                } else { //平滑展开Menu
                                    smoothExpand()
                                }
                            }
                        } else {
                            if (Math.abs(scrollX) > mLimit) { //否则就判断滑动距离
                                //平滑展开Menu
                                smoothExpand()
                            } else { // 平滑关闭Menu
                                smoothClose()
                            }
                        }
                    }
                    //释放
                    releaseVelocityTracker()
                    //LogUtils.i(TAG, "onTouch A ACTION_UP ACTION_CANCEL:velocityY:" + velocityX);
                    isTouching = false //没有手指在摸我了
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        //禁止侧滑时，点击事件不受干扰。
        if (isSwipeEnable) {
            when (ev.action) {
                MotionEvent.ACTION_MOVE ->  //屏蔽滑动时的事件
                    if (Math.abs(ev.rawX - mFirstP.x) > mScaleTouchSlop) {
                        return true
                    }
                MotionEvent.ACTION_UP -> {
                    //为了在侧滑时，屏蔽子View的点击事件
                    if (isLeftSwipe) {
                        if (scrollX > mScaleTouchSlop) { //add by 2016 09 10 解决一个智障问题~ 居然不给点击侧滑菜单 我跪着谢罪
                            //这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的的。。。
                            if (ev.x < width - scrollX) { //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (isUnMoved) {
                                    smoothClose()
                                }
                                return true //true表示拦截
                            }
                        }
                    } else {
                        if (-scrollX > mScaleTouchSlop) {
                            if (ev.x > -scrollX) { //点击范围在菜单外 屏蔽
                                //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (isUnMoved) {
                                    smoothClose()
                                }
                                return true
                            }
                        }
                    }

                    // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (isUserSwiped) {
                        return true
                    }
                }
            }
            //模仿IOS 点击其他区域关闭：
            if (iosInterceptFlag) { //IOS模式开启，且当前有菜单的View，且不是自己的 拦截点击事件给子View
                return true
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun smoothExpand() {

        /*mScroller.startScroll(getScrollX(), 0, mRightMenuWidths - getScrollX(), 0);
        invalidate();*/
        //展开就加入ViewCache：
        mViewCache = this@CustomSwipeMenu
        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView!!.isLongClickable = false
        }
        cancelAnim()
        mExpandAnim = ValueAnimator.ofInt(scrollX,
            if (isLeftSwipe) mRightMenuWidths else -mRightMenuWidths
        )
        mExpandAnim?.addUpdateListener(AnimatorUpdateListener { animation ->
            scrollTo((animation.animatedValue as Int), 0)
        })
        mExpandAnim?.interpolator = OvershootInterpolator()
        mExpandAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isExpand = true
            }
        })
        mExpandAnim?.setDuration(300)?.start()
    }

    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private fun cancelAnim() {
        if (mCloseAnim != null && mCloseAnim!!.isRunning) {
            mCloseAnim!!.cancel()
        }
        if (mExpandAnim != null && mExpandAnim!!.isRunning) {
            mExpandAnim!!.cancel()
        }
    }


    fun printStack(tag: String?) {
        val stack = Thread.currentThread().stackTrace
        val print = StringBuffer()
        print.append("Call ").append(stack[3].methodName).append(":\n")
        for (i in 3 until stack.size) {
            print.append("          at ").append(stack[i].toString()).append("\n")
            if (i > 20) break
        }
        Log.d(tag, print.toString())
    }

    /**
     * 平滑关闭
     */
    fun smoothClose() {
        // mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        // invalidate();
        mViewCache = null
        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView!!.isLongClickable = true
        }
        cancelAnim()
        mCloseAnim = ValueAnimator.ofInt(scrollX, 0)
        mCloseAnim?.addUpdateListener(AnimatorUpdateListener { animation ->
            scrollTo((animation.animatedValue as Int), 0)
        })
        mCloseAnim?.interpolator = AccelerateInterpolator()
        mCloseAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isExpand = false
            }
        })
        mCloseAnim?.setDuration(300)?.start()
    }

    /**
     *向VelocityTracker添加MotionEvent
     */
    private fun acquireVelocityTracker(event: MotionEvent) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
    }

    /**
     * 释放VelocityTracker
     */
    private fun releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker!!.clear()
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    //每次ViewDetach的时候，判断一下 ViewCache是不是自己，如果是自己，关闭侧滑菜单，且ViewCache设置为null，
    // 1 防止内存泄漏(ViewCache是一个静态变量)
    // 2 侧滑删除后自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
    override fun onDetachedFromWindow() {
        if (this === mViewCache) {
            mViewCache!!.smoothClose()
            mViewCache = null
        }
        super.onDetachedFromWindow()
    }

    //展开时，禁止长按
    override fun performLongClick(): Boolean {
        return Math.abs(scrollX) <= mScaleTouchSlop && super.performLongClick()
    }

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    fun setClose() {
        if (this === mViewCache) { //先取消展开动画
            cancelAnim()
            mViewCache!!.scrollTo(0, 0) //关闭
            mViewCache = null
        }
    }


}