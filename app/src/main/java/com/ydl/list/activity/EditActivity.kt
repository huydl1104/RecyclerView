package com.ydl.list.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.ydl.list.R
import com.ydl.listlib.base.AutoCompletePresenter
import com.ydl.listlib.bean.User
import com.ydl.listlib.interfaces.AutoCompleteCallback
import com.ydl.listlib.manager.AutoCompletedManager
import com.ydl.listlib.presenter.UserPresenter
import kotlinx.android.synthetic.main.activity_edit_text.*

/**
 * @author yudongliang
 * create time 2021-09-02
 * describe : EditText 下拉列表
 */
class EditActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        val elevation = 6f
        val backgroundDrawable: Drawable = ColorDrawable(Color.WHITE)
        val presenter: AutoCompletePresenter<User> = UserPresenter(this)
        val callback: AutoCompleteCallback<User> = object : AutoCompleteCallback<User> {
            override fun onPopupItemClicked(editable: Editable, item: User): Boolean {
                editable.clear()
                editable.append(item.fullname)
                return true
            }

            override fun onPopupVisibilityChanged(shown: Boolean) {}
        }

       val userAutocomplete = AutoCompletedManager.on<User>(single)
            .with(elevation)
            .with(backgroundDrawable)
            .with(presenter)
            .with(callback)
            .build()

    }



}