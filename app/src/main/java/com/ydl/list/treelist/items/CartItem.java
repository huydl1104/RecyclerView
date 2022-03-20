package com.ydl.list.treelist.items;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ydl.list.R;
import com.ydl.list.treelist.ViewHolder;
import com.ydl.list.treelist.bean.CartGroupItem3;


public class CartItem extends TreeItem<Integer> {


    @Override
    public int getLayoutId() {
        return R.layout.item_cart_child;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        TreeItemGroup parentItem = getParentItem();
        if (parentItem instanceof CartGroupItem3) {
            viewHolder.setChecked(R.id.cb_ischeck, ((CartGroupItem3) parentItem).isSelect(this));
        }
        viewHolder.setText(R.id.tv_price, data + "");
        viewHolder.itemView.setPadding(50, 0, 0, 0);

    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, RecyclerView.LayoutParams layoutParams, int position) {
        super.getItemOffsets(outRect, layoutParams, position);
        outRect.bottom = 1;
    }

}
