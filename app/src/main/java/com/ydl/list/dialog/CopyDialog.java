package com.ydl.list.dialog;

import android.content.Context;
import android.view.Gravity;

import com.example.base.BaseDialog;
import com.ydl.list.R;


/**
 * 可进行拷贝的副本
 */
public final class CopyDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.copy_dialog);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setGravity(Gravity.BOTTOM);
        }
    }
}