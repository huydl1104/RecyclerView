package com.ydl.list.app

import android.app.Application

class MyApplication :Application() {

    companion object{
        private var application: Application? = null

        fun getApp(): Application? {
            if (application == null) {
                synchronized(MyApplication::class.java) {
                    if (application == null) {
                        application = Application()
                    }
                }
            }
            return application
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}