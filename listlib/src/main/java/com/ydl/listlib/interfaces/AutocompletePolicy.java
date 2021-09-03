package com.ydl.listlib.interfaces;

import android.text.Spannable;

import androidx.annotation.NonNull;


public interface AutocompletePolicy {

    boolean shouldShowPopup(@NonNull Spannable text, int cursorPos);

    boolean shouldDismissPopup(@NonNull Spannable text, int cursorPos);

    @NonNull
    CharSequence getQuery(@NonNull Spannable text);

    void onDismiss(@NonNull Spannable text);
}
