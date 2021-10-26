package com.ydl.list.coordinator.listener;

import com.google.android.material.tabs.TabLayout;

/**
 * @author yudongliang
 * create time 2021-10-18
 * describe :
 */
public interface OnTabSelectedListener {
    public void onTabSelected(TabLayout.Tab tab);

    public void onTabUnselected(TabLayout.Tab tab);

    public void onTabReselected(TabLayout.Tab tab);
}
