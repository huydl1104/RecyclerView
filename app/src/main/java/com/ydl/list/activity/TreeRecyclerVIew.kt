package com.ydl.list.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydl.list.R
import com.ydl.list.treelist.TreeRecyclerAdapter
import com.ydl.list.treelist.bean.CartBean
import com.ydl.list.treelist.bean.CartGroupItem
import com.ydl.list.treelist.bean.TreeSelectItemGroup
import com.ydl.list.treelist.factory.ItemConfig
import com.ydl.list.treelist.factory.ItemHelperFactory
import com.ydl.list.treelist.items.CartItem
import com.ydl.list.treelist.items.TreeItem
import kotlinx.android.synthetic.main.test_tree_recycle.*

class TreeRecyclerVIew : AppCompatActivity() {

    private val mAdapter: TreeRecyclerAdapter = TreeRecyclerAdapter()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_tree_recycle)

        testRecycler.layoutManager = LinearLayoutManager(this)
        testRecycler.adapter = mAdapter

        ItemConfig.register(CartGroupItem::class.java)
        val beans = ArrayList<CartBean>()
        beans.add(CartBean(3))
        Log.e("YUDL", "TreeRecyclerVIew ->$beans")
        val groupItem: List<TreeItem<Any>> = ItemHelperFactory.createItems(beans)

        mAdapter.getItemManager().replaceAllItem(groupItem)
        mAdapter.setOnItemClickListener { viewHolder, position ->
            //因为外部和内部会冲突
            val item = mAdapter.getData(position)
            item?.onClick(viewHolder)
            notifyPrice()
        }
        notifyPrice()

    }


    /**
     * 更新价格
     */
    fun notifyPrice() {
        var price = 0
        for (item in mAdapter.getData()) {
            Log.e("YUDL", "notifyPrice 111 ->$item")
            if (item is TreeSelectItemGroup) {
                Log.e("YUDL", "notifyPrice 222 ->$item")
                val group: TreeSelectItemGroup<Any> = item as TreeSelectItemGroup
                if (!group.isSelect()) { //是否有选择的子类
                    //有一个没选则不是全选
                    continue
                }
                if (!group.isSelectAll()) { //是否全选了子类
                    //有一个没选则不是全选
                }
                val selectItems: List<TreeItem<Any>> = group.getSelectItems()

                for (child in selectItems) {
                    Log.e("YUDL", "notifyPrice 333 ->$child")
                    if (child is CartItem) {
                        val data = child.getData() as Int
                        price += data
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged()

    }



}