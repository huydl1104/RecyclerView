package com.ydl.listlib.manager;


import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import com.ydl.listlib.base.AutoCompletePresenter;
import com.ydl.listlib.extension.PopupConstant;
import com.ydl.listlib.interfaces.AutoCompleteCallback;
import com.ydl.listlib.interfaces.AutocompletePolicy;
import com.ydl.listlib.policy.SimplePolicy;
import com.ydl.listlib.window.AutocompletePopupView;

public class AutoCompletedManager<T> implements TextWatcher, SpanWatcher {

    private AutocompletePolicy policy;
    private AutocompletePopupView popup;
    private AutoCompletePresenter<T> presenter;
    private AutoCompleteCallback<T> callback;
    private EditText source;
    private boolean block;
    private boolean disabled;
    private boolean openBefore;
    private String lastQuery = "null";
    private String TAG = "AutoCompletedManager";

    public AutoCompletedManager(Builder<T> builder) {
        policy = builder.policy;
        presenter = builder.presenter;
        callback = builder.callback;
        source = builder.source;

        // Set up popup
        popup = new AutocompletePopupView(source.getContext());
        popup.setAnchorView(source);
        popup.setGravity(Gravity.START);
        popup.setBackgroundDrawable(builder.backgroundDrawable);
        popup.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, builder.elevationDp,
                source.getContext().getResources().getDisplayMetrics()));

        // popup dimensions
        PopupConstant dim = this.presenter.getConstant();
        popup.setWidth(dim.width);
        popup.setHeight(dim.height);
        popup.setMaxWidth(dim.maxWidth);
        popup.setMaxHeight(dim.maxHeight);

        // Fire visibility events
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lastQuery = "null";
                if (callback != null) callback.onPopupVisibilityChanged(false);
                boolean saved = block;
                block = true;
                policy.onDismiss(source.getText());
                block = saved;
                presenter.hideView();
            }
        });

        // Set up source
        source.getText().setSpan(this, 0, source.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        source.addTextChangedListener(this);

        // Set up presenter
        presenter.registerClickProvider(new AutoCompletePresenter.ClickProvider<T>() {
            @Override
            public void click(@NonNull T item) {
                AutoCompleteCallback<T> callback = AutoCompletedManager.this.callback;
                EditText edit = AutoCompletedManager.this.source;
                if (callback == null) return;
                boolean saved = block;
                block = true;
                boolean dismiss = callback.onPopupItemClicked(edit.getText(), item);
                if (dismiss) dismissPopup();
                block = saved;
            }
        });

        builder.clear();

    }

    public void setInputMethodMode(int mode) {
        popup.setInputMethodMode(mode);
    }
    public void setSoftInputMode(int mode) {
        popup.setSoftInputMode(mode);
    }
    public void showPopup(@NonNull CharSequence query) {
        if (isPopupShowing() && lastQuery.equals(query.toString())) return;
        lastQuery = query.toString();

        Log.i(TAG,"showPopup: called with filter "+query);
        if (!isPopupShowing()) {
            Log.i(TAG,"showPopup: showing");
            presenter.registerDataSetObserver(new MyObserver()); // Calling new to avoid leaking... maybe...
            popup.setView(presenter.getView());
            presenter.showView();
            popup.show();
            if (callback != null) callback.onPopupVisibilityChanged(true);
        }
        Log.i(TAG,"showPopup: popup should be showing... "+isPopupShowing());
        presenter.onQuery(query);
    }

    public void dismissPopup() {
        if (isPopupShowing()) {
            popup.dismiss();
        }
    }

    public boolean isPopupShowing() {
        return this.popup.isShowing();
    }


    public void setEnabled(boolean enabled) {
        disabled = !enabled;
    }


    public void setGravity(int gravity) {
        popup.setGravity(gravity);
    }


    public void setOffsetFromAnchor(int offset) { popup.setVerticalOffset(offset); }

    public void setOutsideTouchable(boolean outsideTouchable) { popup.setOutsideTouchable(outsideTouchable); }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (block || disabled) return;
        openBefore = isPopupShowing();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (block || disabled) return;
        if (openBefore && !isPopupShowing()) {
            return; // Copied from somewhere.
        }
        if (!(s instanceof Spannable)) {
            source.setText(new SpannableString(s));
            return;
        }
        Spannable sp = (Spannable) s;

        int cursor = source.getSelectionEnd();
        Log.e(TAG,"onTextChanged: cursor end position is "+cursor);
        if (cursor == -1) { // No cursor present.
            dismissPopup(); return;
        }
        if (cursor != source.getSelectionStart()) {
            // Not sure about this. We should have no problems dealing with multi selections,
            // we just take the end...
            // dismissPopup(); return;
        }

        boolean b = block;
        block = true; // policy might add spans or other stuff.
        if (isPopupShowing() && policy.shouldDismissPopup(sp, cursor)) {
            Log.i(TAG,"onTextChanged: dismissing");
            dismissPopup();
        } else if (isPopupShowing() || policy.shouldShowPopup(sp, cursor)) {
            // LOG.now("onTextChanged: updating with filter "+policy.getQuery(sp));
            showPopup(policy.getQuery(sp));
        }
        block = b;
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onSpanAdded(Spannable text, Object what, int start, int end) {}

    @Override
    public void onSpanRemoved(Spannable text, Object what, int start, int end) {}

    @Override
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
        if (disabled || block) return;
        if (what == Selection.SELECTION_END) {
            // Selection end changed from ostart to nstart. Trigger a check.
            Log.i(TAG,"onSpanChanged: selection end moved from "+ostart+" to "+nstart);
            Log.i(TAG,"onSpanChanged: block is "+block);
            boolean b = block;
            block = true;
            if (!isPopupShowing() && policy.shouldShowPopup(text, nstart)) {
                showPopup(policy.getQuery(text));
            }
            block = b;
        }
    }

    private class MyObserver extends DataSetObserver implements Runnable {
        private Handler ui = new Handler(Looper.getMainLooper());

        @Override
        public void onChanged() {
            ui.post(this);
        }

        @Override
        public void run() {
            if (isPopupShowing()) {
                popup.show();
            }
        }
    }

    public static <T> Builder<T> on(EditText anchor) {
        return new Builder<T>(anchor);
    }

    public final static class Builder<T>{
        private EditText source;
        private AutoCompletePresenter<T> presenter;
        private AutocompletePolicy policy;
        private AutoCompleteCallback<T> callback;
        private Drawable backgroundDrawable;
        private float elevationDp = 6;
        private Builder(EditText source) {
            this.source = source;
        }

        public Builder<T> with(AutoCompletePresenter<T> presenter) {
            this.presenter = presenter;
            return this;
        }

        public Builder<T> with(AutoCompleteCallback<T> callback) {
            this.callback = callback;
            return this;
        }

        public Builder<T> with(AutocompletePolicy policy) {
            this.policy = policy;
            return this;
        }

        public Builder<T> with(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder<T> with(float elevationDp) {
            this.elevationDp = elevationDp;
            return this;
        }

        public AutoCompletedManager<T> build() {
            if (source == null) throw new RuntimeException("Autocomplete needs a source!");
            if (presenter == null) throw new RuntimeException("Autocomplete needs a presenter!");
            if (policy == null) policy = new SimplePolicy();
            return new AutoCompletedManager<T>(this);
        }

        private void clear() {
            source = null;
            presenter = null;
            callback = null;
            policy = null;
            backgroundDrawable = null;
            elevationDp = 6;
        }
    }
}
