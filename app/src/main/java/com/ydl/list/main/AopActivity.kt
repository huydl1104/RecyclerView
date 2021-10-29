package com.ydl.list.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ydl.list.R
import com.ydl.list.aop.actions.CheckNet
import kotlinx.android.synthetic.main.activity_aop.*

class AopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aop)
        button.setOnClickListener {
            Log.e("YUDL", "--- button click ---")
            test()
        }
    }
    @CheckNet
    fun test(){
        Log.e("YUDL", "--- test exe ---")
    }
}