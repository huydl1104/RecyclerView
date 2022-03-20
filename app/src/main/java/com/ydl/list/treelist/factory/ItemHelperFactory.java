package com.ydl.list.treelist.factory;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ydl.list.treelist.TreeRecyclerType;
import com.ydl.list.treelist.annotation.TreeItemType;
import com.ydl.list.treelist.items.TreeItem;
import com.ydl.list.treelist.items.TreeItemGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ItemHelperFactory {
    private static final HashMap<Class, Class<? extends TreeItem>> classCacheMap = new HashMap<>();

    public static List<TreeItem> createItems(@Nullable List list) {
        return createItems(list, null, null);
    }

    public static List<TreeItem> createItems(@Nullable List list, @Nullable TreeItemGroup treeParentItem) {
        return createItems(list, null, treeParentItem);
    }

    public static List<TreeItem> createItems(@Nullable List list, Class<? extends TreeItem> iClass) {
        return createItems(list, iClass, null);
    }

    public static List<TreeItem> createItems(@Nullable List list, Class<? extends TreeItem> iClass, @Nullable TreeItemGroup treeParentItem) {
        if (null == list) {
            return null;
        }
        int size = list.size();
        ArrayList<TreeItem> treeItemList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Object itemData = list.get(i);
            TreeItem treeItem = createItem(itemData, iClass, treeParentItem);
            if (treeItem != null) {
                treeItemList.add(treeItem);
            }
        }
        return treeItemList;
    }

    /**
     * 确定item的class类型,并且添加到了itemConfig,用该方法创建TreeItem
     *
     * @return
     */
    public static TreeItem createItem(Object d) {
        return createItem(d, null, null);
    }

    @Nullable
    public static TreeItem createItem(Object data, @Nullable TreeItemGroup treeParentItem) {
        return createItem(data, null, treeParentItem);
    }

    @Nullable
    public static TreeItem createItem(Object data, @Nullable Class zClass) {
        return createItem(data, zClass, null);
    }

    @Nullable
    public static TreeItem createItem(Object data, @Nullable Class zClass, @Nullable TreeItemGroup treeParentItem) {
        TreeItem treeItem = null;
        Class<? extends TreeItem> treeItemClass;
        try {
            if (zClass != null) {
                treeItemClass = zClass;
                ItemConfig.register(zClass);
            } else {
                treeItemClass = ItemConfig.getTypeClass(data);
                //判断是否是TreeItem的子类
            }
            if (treeItemClass != null) {
                treeItem = treeItemClass.newInstance();
                treeItem.setData(data);
                treeItem.setParentItem(treeParentItem);
                TreeItemType annotation = treeItemClass.getAnnotation(TreeItemType.class);
                if (annotation != null) {
                    int spanSize = annotation.spanSize();
                    treeItem.setSpanSize(spanSize);
                }
            }
        } catch (ClassCastException e) {
            Log.w("ClassCastException", "传入的data与item定义的data泛型不一致");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeItem;
    }


    /**
     * 根据TreeRecyclerType获取子item集合,不包含TreeItemGroup自身
     *
     * @param itemGroup
     * @param type
     * @return
     */
    @NonNull
    public static ArrayList<TreeItem> getChildItemsWithType(@Nullable TreeItemGroup itemGroup, @NonNull TreeRecyclerType type) {
        if (itemGroup == null) {
            return new ArrayList();
        }
        return getChildItemsWithType(itemGroup.getChild(), type);
    }

    @NonNull
    public static ArrayList<TreeItem> getChildItemsWithType(@Nullable List<TreeItem> items, @NonNull TreeRecyclerType type) {
        ArrayList<TreeItem> returnItems = new ArrayList<>();
        if (items == null) {
            return returnItems;
        }
        int childCount = items.size();
        for (int i = 0; i < childCount; i++) {
            TreeItem childItem = items.get(i);//获取当前一级
            returnItems.add(childItem);
            if (childItem instanceof TreeItemGroup) {//获取下一级
                List list = null;
                switch (type) {
                    case SHOW_ALL:
                        //调用下级的getAllChild遍历,相当于递归遍历
                        list = getChildItemsWithType((TreeItemGroup) childItem, type);
                        break;
                    case SHOW_EXPAND:
                        //根据isExpand,来决定是否展示
                        if (((TreeItemGroup) childItem).isExpand()) {
                            list = getChildItemsWithType((TreeItemGroup) childItem, type);
                        }
                        break;
                }
                if (list != null && list.size() > 0) {
                    returnItems.addAll(list);
                }
            }
        }
        return returnItems;
    }
}
