package com.ydl.listlib.window;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.view.ViewCompat;
import androidx.core.widget.PopupWindowCompat;

/**
 * @author yudongliang
 * create time 2021-09-02
 * describe : 自定义PopupWindow
 *
 * window type：
 * WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW：第一个普通的应用窗口
 * WindowManager.LayoutParams.TYPE_APPLICATION：普通应用窗口（activity的默认窗口类型）
 * WindowManager.LayoutParams.LAST_APPLICATION_WINDOW：最后一个应用窗口
 * FIRST_SUB_WINDOW = 1000 ：第一个子窗口
 * TYPE_APPLICATION_PANEL = FIRST_SUB_WINDOW：应用窗口的子窗口，PopupWindow的默认类型
 * TYPE_APPLICATION_SUB_PANEL = FIRST_SUB_WINDOW + 2：TYPE_APPLICATION_PANEL的子窗口
 */
public class AutocompletePopupView {

    private Context mContext;
    private ViewGroup mView;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mMaxHeight = Integer.MAX_VALUE;
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mUserMaxHeight = Integer.MAX_VALUE;
    private int mUserMaxWidth = Integer.MAX_VALUE;
    private int mHorizontalOffset = 0;
    private int mVerticalOffset = 0;
    private boolean mVerticalOffsetSet;
    private int mGravity = Gravity.NO_GRAVITY;
    private boolean mAlwaysVisible = false;
    private boolean mOutsideTouchable = true;
    private View mAnchorView;
    private final Rect mTempRect = new Rect();
    private PopupWindow mPopup;

    public AutocompletePopupView(@NonNull Context context) {
        super();
        mContext = context;
        mPopup = new PopupWindow(context);
        mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    }

    /**
     * 设置popupWindow的焦点状态
     */
    public AutocompletePopupView setFocusable(boolean focusable) {
        mPopup.setFocusable(focusable);
        return this;
    }
    /**
     * 设置popupWindow的阴影大小
     */
    public AutocompletePopupView setElevation(float elevationPx) {
        mPopup.setElevation(elevationPx);
        return this;
    }
    /**
     * 设置popupWindow的显示的位置
     */
    public AutocompletePopupView setGravity(int gravity){
        mGravity = gravity;
        return this;
    }
    /**
     * 设置popupWindow的宽度
     */
    public void setWidth(int width) {
        mWidth = width;
    }
    /**
     * 设置popupWindow的的显示状态
     */
    public AutocompletePopupView setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        mAlwaysVisible = dropDownAlwaysVisible;
        return this;
    }
    /**
     * 设置popupWindow的状态，点击外部隐藏
     */
    public AutocompletePopupView setOutsideTouchable(boolean outsideTouchable) {
        mOutsideTouchable = outsideTouchable;
        return this;
    }
    /**
     * 设置popupWindow的软键盘模式
     * @param mode 软键盘模式
     */
    public AutocompletePopupView setSoftInputMode(int mode) {
        mPopup.setSoftInputMode(mode);
        return this;
    }
    /**
     * background
     */
    public AutocompletePopupView setBackgroundDrawable(@Nullable Drawable d) {
        mPopup.setBackgroundDrawable(d);
        return this;
    }

    /**
     * set animation style
     */
    public AutocompletePopupView setAnimationStyle(@StyleRes int animationStyle) {
        mPopup.setAnimationStyle(animationStyle);
        return this;
    }

    /**
     * set anchor view
     */
    public AutocompletePopupView setAnchorView(@NonNull View anchor) {
        mAnchorView = anchor;
        return this;
    }

    /**
     * 设置水平的偏移量
     * @param offset 偏移size
     */
    public AutocompletePopupView setHorizontalOffset(int offset) {
        mHorizontalOffset = offset;
        return this;
    }
    /**
     * 设置垂直的偏移量
     * @param offset 偏移size
     */
    public AutocompletePopupView setVerticalOffset(int offset) {
        mVerticalOffset = offset;
        mVerticalOffsetSet = true;
        return this;
    }

    /**
     * 设置正文的宽度
     * @param width 宽度的size
     */
    public AutocompletePopupView setContentWidth(int width) {
        Drawable popupBackground = mPopup.getBackground();
        if (popupBackground != null) {
            popupBackground.getPadding(mTempRect);
            width += mTempRect.left + mTempRect.right;
        }
        setWidth(width);
        return this;
    }

    /**
     * 设置使用的最大的宽度
     * @param width 宽度的size
     */
    public AutocompletePopupView setMaxWidth(int width) {
        if (width > 0) {
            mUserMaxWidth = width;
        }
        return this;
    }

    /**
     * 设置高度
     * @param height 高度的size
     */
    public AutocompletePopupView setHeight(int height) {
        mHeight = height;
        return this;
    }

    /**
     * 设置内容高度
     * @param height 高度的size
     */
    public AutocompletePopupView setContentHeight(int height) {
        Drawable popupBackground = mPopup.getBackground();
        if (popupBackground != null) {
            popupBackground.getPadding(mTempRect);
            height += mTempRect.top + mTempRect.bottom;
        }
        setHeight(height);
        return this;
    }
    /**
     * 设置最大高度
     * @param height 高度的size
     */
    public AutocompletePopupView setMaxHeight(int height) {
        if (height > 0) {
            mUserMaxHeight = height;
        }
        return this;
    }

    /**
     * 设置监听回调函数
     * @param listener 监听回调函数
     */
    public AutocompletePopupView setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mPopup.setOnDismissListener(listener);
        return this;
    }

    /**
     * 设置popupWindow中的view
     * @param view
     * @return
     */
    public AutocompletePopupView setView(ViewGroup view) {
        mView = view;
        mView.setFocusable(true);
        mView.setFocusableInTouchMode(true);
        ViewGroup dropDownView = mView;
        mPopup.setContentView(dropDownView);
        ViewGroup.LayoutParams params = mView.getLayoutParams();
        if (params != null) {
            if (params.height > 0) setHeight(params.height);
            if (params.width > 0) setWidth(params.width);
        }
        return this;
    }

    public void setInputMethodMode(int mode) {
        mPopup.setInputMethodMode(mode);
    }

    /**
     * 放在view 下方的破普皮window是否显示
     * @return true：显示；false：隐藏
     */
    boolean isDropDownAlwaysVisible() {
        return mAlwaysVisible;
    }

    /**
     * 校验是否是外部触摸事件
     * @return true：确认触发
     */
    boolean isOutsideTouchable() {
        return mOutsideTouchable && !mAlwaysVisible;
    }

    /**
     * popupWindow 的软键盘模式
     */
    int getSoftInputMode() {
        return mPopup.getSoftInputMode();
    }

    /**
     * 获取background
     */
    Drawable getBackground() {
        return mPopup.getBackground();
    }

    /**
     * get animation style
     */
    @StyleRes
    int getAnimationStyle() {
        return mPopup.getAnimationStyle();
    }

    /**
     * get anchor view
     */
    View getAnchorView() {
        return mAnchorView;
    }

    /**
     * get popupWindow width
     */
    int getWidth() {
        return mWidth;
    }

    /**
     * get popupWindow height
     */
    int getHeight() {
        return mHeight;
    }

    /**
     * 检测是否是不需要软件盘的的模式
     * @return
     */
    boolean isInputMethodNotNeeded() {
        return mPopup.getInputMethodMode() == PopupWindow.INPUT_METHOD_NOT_NEEDED;
    }

    /**
     * 检测window是否已经显示
     * @return true 显示； false：隐藏
     */
    public boolean isShowing() {
        return mPopup.isShowing();
    }


    /**
     * 构建弹出窗口的内容，并返回弹出窗口的高度，当内容已经存在时返回-1。
     */
    private int buildDropDown() {
        int otherHeights = 0;

        final int paddingVert;
        final int paddingHoriz;
        final Drawable background = mPopup.getBackground();
        //判断是否有背景图
        if (background != null) {
            //得到图片的 mTempRect，来计算 background size
            background.getPadding(mTempRect);
            paddingVert = mTempRect.top + mTempRect.bottom;
            paddingHoriz = mTempRect.left + mTempRect.right;
            //校验是否设置了垂直的偏移量
            if (!mVerticalOffsetSet) {
                mVerticalOffset = -mTempRect.top;
            }
        } else {
            //没有图片设置 mTempRect 为空，水平和垂直的padding为0；
            mTempRect.setEmpty();
            paddingVert = 0;
            paddingHoriz = 0;
        }

        //这个模式的弹框不需要输入法
        final boolean ignoreBottomDecorations = mPopup.getInputMethodMode() == PopupWindow.INPUT_METHOD_NOT_NEEDED;
        //得到最大的可用的高度
        final int maxContentHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                mPopup.getMaxAvailableHeight(getAnchorView(), mVerticalOffset, ignoreBottomDecorations) :
                mPopup.getMaxAvailableHeight(getAnchorView(), mVerticalOffset);
        //得到最大的可用的宽度
        final int maxContentWidth = mContext.getResources().getDisplayMetrics().widthPixels - paddingHoriz;

        mMaxHeight = Math.min(maxContentHeight + paddingVert, mUserMaxHeight);
        mMaxWidth = Math.min(maxContentWidth + paddingHoriz, mUserMaxWidth);

        //总是显示window的情况下，就不需要去动态的测量内部view的宽高
        if (mAlwaysVisible || mHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
            return mMaxHeight;
        }

        final int childWidthSpec;
        switch (mWidth) {
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                childWidthSpec = View.MeasureSpec.makeMeasureSpec(maxContentWidth, View.MeasureSpec.AT_MOST); break;
            case ViewGroup.LayoutParams.MATCH_PARENT:
                childWidthSpec = View.MeasureSpec.makeMeasureSpec(maxContentWidth, View.MeasureSpec.EXACTLY); break;
            default:
                childWidthSpec = View.MeasureSpec.makeMeasureSpec(mWidth, View.MeasureSpec.EXACTLY); break;
        }
        //测量mView的宽高
        mView.measure(childWidthSpec, View.MeasureSpec.makeMeasureSpec(maxContentHeight, View.MeasureSpec.AT_MOST));
        final int viewHeight = mView.getMeasuredHeight();
        if (viewHeight > 0) {
            otherHeights += paddingVert + mView.getPaddingTop() + mView.getPaddingBottom();
        }
        return Math.min(viewHeight + otherHeights, mMaxHeight);
    }

    public void show() {
        //anchor是否已经添加到window中
        if (!ViewCompat.isAttachedToWindow(getAnchorView())) return;

        int height = buildDropDown();
        final boolean noInputMethod = isInputMethodNotNeeded();
        //应用window上的一个子窗口
        int mDropDownWindowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
        PopupWindowCompat.setWindowLayoutType(mPopup, mDropDownWindowLayoutType);

        if (isShowing()) {

            if (mHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                int tempWidth = mWidth == ViewGroup.LayoutParams.MATCH_PARENT ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
                if (noInputMethod) {
                    mPopup.setWidth(tempWidth);
                    mPopup.setHeight(0);
                } else {
                    mPopup.setWidth(tempWidth);
                    mPopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }

            int widthSpec;
            if (mWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                widthSpec = -1;
            } else if (mWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                widthSpec = getAnchorView().getWidth();
            } else {
                widthSpec = mWidth;
            }
            widthSpec = Math.min(widthSpec, mMaxWidth);
            widthSpec = (widthSpec < 0) ? - 1 : widthSpec;

            // Height.
            int heightSpec;
            if (mHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                heightSpec = noInputMethod ? height : ViewGroup.LayoutParams.MATCH_PARENT;
            } else if (mHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                heightSpec = height;
            } else {
                heightSpec = mHeight;
            }
            heightSpec = Math.min(heightSpec, mMaxHeight);
            heightSpec = (heightSpec < 0) ? - 1 : heightSpec;

            // Update.
            mPopup.setOutsideTouchable(isOutsideTouchable());
            if (heightSpec == 0) {
                dismiss();
            } else {
                mPopup.update(getAnchorView(), mHorizontalOffset, mVerticalOffset, widthSpec, heightSpec);
            }

        } else {
            int widthSpec;
            if (mWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                widthSpec = ViewGroup.LayoutParams.MATCH_PARENT;
            } else if (mWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                widthSpec = getAnchorView().getWidth();
            } else {
                widthSpec = mWidth;
            }
            widthSpec = Math.min(widthSpec, mMaxWidth);

            int heightSpec;
            if (mHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                heightSpec = ViewGroup.LayoutParams.MATCH_PARENT;
            } else if (mHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                heightSpec = height;
            } else {
                heightSpec = mHeight;
            }
            heightSpec = Math.min(heightSpec, mMaxHeight);

            // Set width and height.
            mPopup.setWidth(widthSpec);
            mPopup.setHeight(heightSpec);
            mPopup.setClippingEnabled(true);

            mPopup.setOutsideTouchable(isOutsideTouchable());
            PopupWindowCompat.showAsDropDown(mPopup, getAnchorView(), mHorizontalOffset, mVerticalOffset, mGravity);
        }
    }

    public void dismiss() {
        mPopup.dismiss();
        mPopup.setContentView(null);
        mView = null;
    }


}
