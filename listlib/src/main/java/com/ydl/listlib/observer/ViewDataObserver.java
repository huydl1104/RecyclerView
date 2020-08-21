
package com.ydl.listlib.observer;


import androidx.recyclerview.widget.RecyclerView;

import com.ydl.listlib.adapter.BaseRecyclerAdapter;
import com.ydl.listlib.view.CustomRefreshView;

public class ViewDataObserver extends RecyclerView.AdapterDataObserver {

    private CustomRefreshView recyclerView;
    private BaseRecyclerAdapter adapter;
    public ViewDataObserver(CustomRefreshView recyclerView) {
        this.recyclerView = recyclerView;
        if (recyclerView.getAdapter() instanceof BaseRecyclerAdapter) {
            adapter = (BaseRecyclerAdapter) recyclerView.getAdapter();
        }
    }

    private boolean isHeaderFooter(int position) {
        return adapter != null && (position < adapter.getHeaderCount()
                || position >= adapter.getHeaderCount() + adapter.getMiddleCount());
    }


    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        //header&footer不会有移动操作
        update();
    }

    @Override
    public void onChanged() {
        super.onChanged();
        //header&footer不会引起changed
        update();
    }


    private void update() {
        int count;
        if (recyclerView.getAdapter() instanceof BaseRecyclerAdapter) {
            count = ((BaseRecyclerAdapter) recyclerView.getAdapter()).getMiddleCount();
        } else {
            count = recyclerView.getAdapter().getItemCount();
        }
        if (count == 0) {
            recyclerView.showEmpty();
        } else {
            recyclerView.showRecycler();
        }
    }

}