package com.ydl.list.app

import android.app.Application
import com.hjq.bar.TitleBar
import com.hjq.toast.ToastUtils
import com.ydl.list.manager.ActivityManager
import com.ydl.list.toast.ToastLogInterceptor
import com.ydl.list.toast.ToastStyle
import com.ydl.list.utils.DebugLoggerTree
import com.ydl.list.utils.LogCustomUtils
import com.ydl.list.crash.CrashHandler
import com.ydl.list.titlebar.TitleBarStyle

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
        ActivityManager.getInstance().init(this)
        initToast()
        initLog()
        initCrash()
        initTitleBar()
    }

    private fun initToast() {
        // 初始化吐司
        ToastUtils.init(application, ToastStyle())
        // 设置调试模式
        ToastUtils.setDebugMode(AppConfig.isDebug())
        // 设置 Toast 拦截器
        ToastUtils.setInterceptor(ToastLogInterceptor())
    }
    private fun initLog(){
        // 初始化日志打印
        if (AppConfig.isLogEnable()) {
            LogCustomUtils.plant(DebugLoggerTree())
        }
    }
    private fun initCrash(){
        // 本地异常捕捉
        CrashHandler.register(this)
    }
    private fun initTitleBar(){
        // 设置标题栏初始化器
        TitleBar.setDefaultStyle(TitleBarStyle())
    }
}