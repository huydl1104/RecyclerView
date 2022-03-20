package com.ydl.list.treelist.bean;

import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ydl.list.R;
import com.ydl.list.activity.TreeRecyclerVIew;
import com.ydl.list.treelist.ViewHolder;
import com.ydl.list.treelist.annotation.TreeItemType;
import com.ydl.list.treelist.factory.ItemHelperFactory;
import com.ydl.list.treelist.items.CartItem;
import com.ydl.list.treelist.items.TreeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@TreeItemType(type = "1")
public class CartGroupItem3 extends TreeSelectItemGroup<CartBean.CartBean2.CartBean3> {

    @Nullable
    @Override
    protected List<TreeItem> initChild(CartBean.CartBean2.CartBean3 data) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < data.childSum; i++) {
            list.add(new Random().nextInt(300));
        }
        return ItemHelperFactory.createItems(list, CartItem.class, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_cart_group;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.setText(R.id.cb_ischeck, "联想(三级)");
        viewHolder.setChecked(R.id.cb_ischeck, isSelect());
        viewHolder.<CheckBox>getView(R.id.cb_ischeck).setOnClickListener((v) -> {
            selectAll(!isSelectAll(), true);
            ((TreeRecyclerVIew) viewHolder.itemView.getContext()).notifyPrice();
        });
        viewHolder.itemView.setPadding(40, 0, 0, 0);
    }

    @Override
    public boolean onInterceptClick(TreeItem child) {
        selectItem(child, true);
        return super.onInterceptClick(child);
    }
}
