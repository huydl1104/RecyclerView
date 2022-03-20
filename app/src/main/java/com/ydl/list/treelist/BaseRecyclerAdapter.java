package com.ydl.list.treelist;

import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected ItemManager<T> mItemManager;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    private List<T> data;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(parent, viewType);
        onBindViewHolderClick(holder, holder.itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBindViewHolder(holder, getData(position), position);
    }

    /**
     * 实现item的点击事件
     */
    public void onBindViewHolderClick(@NonNull final ViewHolder viewHolder, View view) {
        //判断当前holder是否已经设置了点击事件
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获得holder的position
                    int layoutPosition = viewHolder.getLayoutPosition();
                    //检查item的position,是否可以点击.
//                  检查并得到真实的position
                    int itemPosition = getItemManager().itemToDataPosition(layoutPosition);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(viewHolder, itemPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutId(position);
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }


    public int getItemSpanSize(int position, int maxSpan) {
        return maxSpan;
    }

    public List<T> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<T> data) {
        if (data != null) {
            getData().clear();
            getData().addAll(data);
        }
    }

    @Nullable
    public T getData(int position) {
        if (position >= 0) {
            return getData().get(position);
        }
        return null;
    }

    /**
     * 直接强转为指定类型,可以能null
     *
     * @param position
     * @param <D>
     * @return
     */
    public <D> D getCastData(int position) {
        try {
            return (D) getData(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 操作adapter
     *
     * @return
     */
    public ItemManager<T> getItemManager() {
        if (mItemManager == null) {
            mItemManager = new ItemManageImpl<T>(this);
        }
        return mItemManager;
    }

    public void setItemManager(ItemManager<T> itemManager) {
        mItemManager = itemManager;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(@NonNull ViewHolder viewHolder, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(@NonNull ViewHolder viewHolder, int position);
    }

    //检查当前position,获取原始角标
    public int checkPosition(int position) {
        return getItemManager().itemToDataPosition(position);
    }


    /**
     * 获取该position的item的layout
     *
     * @param position 角标
     * @return item的layout id
     */
    public abstract int getLayoutId(int position);

    /**
     * view与数据绑定
     *
     * @param holder   ViewHolder
     * @param t        数据类型
     * @param position position
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, T t, int position) {

    }

    public void clear() {
        getData().clear();
    }
}
