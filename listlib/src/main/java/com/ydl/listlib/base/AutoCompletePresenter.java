package com.ydl.listlib.base;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ydl.listlib.extension.PopupConstant;


public abstract class AutoCompletePresenter<T> {

    private Context mContext;
    private boolean isShowing;
    public AutoCompletePresenter(Context context){
        this.mContext = context;
    }

    public void registerClickProvider(ClickProvider<T> provider){ }

    public void registerDataSetObserver(@NonNull DataSetObserver observer) { }

    @NonNull
    public abstract ViewGroup getView();

    public PopupConstant getConstant() { return new PopupConstant(); }

    public abstract void onQuery(@Nullable CharSequence query);

    protected abstract void onViewShow();

    protected abstract void onViewHidden();

    protected Context getContext(){ return mContext; }

    protected final boolean isShowing() {
        return isShowing;
    }

    public final void showView() {
        isShowing = true;
        onViewShow();
    }

    public final void hideView() {
        isShowing = false;
        onViewHidden();
    }

    public interface ClickProvider<T> {
        void click(@NonNull T item);
    }
}
