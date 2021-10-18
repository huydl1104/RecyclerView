package com.ydl.list.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.ydl.list.R;


/**
 * 引导页适配器
 */
public final class GuideAdapter extends AppAdapter<Integer> {

    public GuideAdapter(Context context) {
        super(context);
        addItem(R.drawable.icon_1);
        addItem(R.drawable.icon_2);
        addItem(R.drawable.icon_3);
        addItem(R.drawable.icon_4);
        addItem(R.drawable.icon_5);
        addItem(R.drawable.icon_6);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final ImageView mImageView;

        private ViewHolder() {
            super(R.layout.guide_item);
            mImageView = (ImageView) getItemView();
        }

        @Override
        public void onBindView(int position) {
            mImageView.setImageResource(getItem(position));
        }
    }
}