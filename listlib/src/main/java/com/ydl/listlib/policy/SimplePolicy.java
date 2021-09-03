package com.ydl.listlib.policy;

import android.text.Spannable;

import androidx.annotation.NonNull;

import com.ydl.listlib.interfaces.AutocompletePolicy;

/**
 * @author yudongliang
 * create time 2021-09-03
 * describe : 简单的一些策略
 */
public class SimplePolicy implements AutocompletePolicy {
    @Override
    public boolean shouldShowPopup(@NonNull Spannable text, int cursorPos) {
        return text.length() > 0;
    }

    @Override
    public boolean shouldDismissPopup(@NonNull Spannable text, int cursorPos) {
        return text.length() == 0;
    }

    @NonNull
    @Override
    public CharSequence getQuery(@NonNull Spannable text) {
        return text;
    }

    @Override
    public void onDismiss(@NonNull Spannable text) {}
}
