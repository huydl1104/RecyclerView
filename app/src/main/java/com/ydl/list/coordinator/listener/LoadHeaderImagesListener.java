package com.ydl.list.coordinator.listener;

import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

/**
 * @author yudongliang
 * create time 2021-10-18
 * describe :
 */
public interface LoadHeaderImagesListener {
    void loadHeaderImages(ImageView imageView, TabLayout.Tab tab);
}
