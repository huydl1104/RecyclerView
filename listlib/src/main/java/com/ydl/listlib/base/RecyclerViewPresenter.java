package com.ydl.listlib.base;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class RecyclerViewPresenter<T> extends AutoCompletePresenter<T>{

    private ClickProvider<T> clicks;
    private Observer observer;
    private RecyclerView recyclerView;

    public RecyclerViewPresenter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewGroup getView() {
        RecyclerView.Adapter recyclerAdapter = getRecyclerAdapter();
        if (recyclerView == null){
            recyclerView = new RecyclerView(getContext());
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(getLayoutManager());
        }
        if (observer != null){
            recyclerAdapter.registerAdapterDataObserver(observer);
            observer = null;
        }
        return recyclerView;
    }

    protected abstract RecyclerView.Adapter getRecyclerAdapter();

    protected RecyclerView.LayoutManager getLayoutManager(){
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public final void registerClickProvider(@NonNull ClickProvider<T> provider) {
        this.clicks = provider;
    }

    @Override
    public final void registerDataSetObserver(@NonNull DataSetObserver observer) {
        this.observer = new Observer(observer);
    }

    @Nullable
    protected final RecyclerView getRecyclerView() {
        return recyclerView;
    }

    protected final void dispatchClick(@NonNull T item) {
        if (clicks != null) clicks.click(item);
    }

    protected final void dispatchLayoutChange() {
        if (observer != null) observer.onChanged();
    }

    @Override
    public void onQuery(@Nullable CharSequence query) {

    }

    @Override
    protected void onViewShow() {

    }

    @Override
    protected void onViewHidden() {
        recyclerView = null;
        observer = null;
    }

    static class Observer extends RecyclerView.AdapterDataObserver{

        private final DataSetObserver mObserver;

        public Observer(DataSetObserver observer) {
            this.mObserver = observer;
        }
        @Override
        public void onChanged() {
            mObserver.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mObserver.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mObserver.onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mObserver.onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mObserver.onChanged();
        }
    }

}
