package com.ydl.list.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.hjq.bar.TitleBar
import com.hjq.gson.factory.GsonFactory
import com.hjq.gson.factory.JsonCallback
import com.hjq.http.EasyConfig
import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestInterceptor
import com.hjq.http.model.HttpHeaders
import com.hjq.http.model.HttpParams
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator
import com.scwang.smart.refresh.layout.listener.DefaultRefreshInitializer
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.ydl.list.R
import com.ydl.list.manager.ActivityManager
import com.ydl.list.toast.ToastLogInterceptor
import com.ydl.list.toast.ToastStyle
import com.ydl.list.utils.DebugLoggerTree
import com.ydl.list.utils.LogCustomUtils
import com.ydl.list.crash.CrashHandler
import com.ydl.list.http.model.RequestHandler
import com.ydl.list.http.model.RequestServer
import com.ydl.list.titlebar.TitleBarStyle
import okhttp3.OkHttpClient
import java.lang.IllegalArgumentException
import java.lang.String

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
        initsdk()
    }

    private fun initsdk() {
        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { cx: Context?, layout: RefreshLayout? ->
            MaterialHeader(application).setColorSchemeColors(
                ContextCompat.getColor(
                    application!!, R.color.common_accent_color
                )
            )
        }
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { cx: Context?, layout: RefreshLayout? ->
            SmartBallPulseFooter(application)
        }
        // 设置全局初始化器
        SmartRefreshLayout.setDefaultRefreshInitializer { cx: Context?, layout: RefreshLayout ->
            // 刷新头部是否跟随内容偏移
            layout.setEnableHeaderTranslationContent(true) // 刷新尾部是否跟随内容偏移
                .setEnableFooterTranslationContent(true) // 加载更多是否跟随内容偏移
                .setEnableFooterFollowWhenNoMoreData(true) // 内容不满一页时是否可以上拉加载更多
                .setEnableLoadMoreWhenContentNotFull(false) // 仿苹果越界效果开关
                .setEnableOverScrollDrag(false)
        }

        // MMKV 初始化
        MMKV.initialize(application)

        // 网络请求框架初始化
        val okHttpClient = OkHttpClient.Builder()
            .build()

        EasyConfig.with(okHttpClient) // 是否打印日志
            .setLogEnabled(AppConfig.isLogEnable()) // 设置服务器配置
            .setServer(RequestServer()) // 设置请求处理策略
            .setHandler(RequestHandler(application)) // 设置请求重试次数
            .setRetryCount(1)
            .setInterceptor(IRequestInterceptor { api: IRequestApi?, params: HttpParams?, headers: HttpHeaders ->
                // 添加全局请求头
                headers.put("token", "66666666666")
//                headers.put("deviceOaid", UmengClient.getDeviceOaid())
                headers.put("versionName", AppConfig.getVersionName())
                headers.put("versionCode", String.valueOf(AppConfig.getVersionCode()))
            })
            .into()

        // 设置 Json 解析容错监听
        GsonFactory.setJsonCallback { typeToken: TypeToken<*>, fieldName: kotlin.String, jsonToken: JsonToken ->
            // 上报到 Bugly 错误列表
            CrashReport.postCatchedException(
                IllegalArgumentException(
                    "类型解析异常：$typeToken#$fieldName，后台返回的类型为：$jsonToken"
                )
            )
        }

        // 初始化日志打印
        if (AppConfig.isLogEnable()) {
            LogCustomUtils.plant(DebugLoggerTree())
        }

        // 注册网络状态变化监听
        val connectivityManager = ContextCompat.getSystemService(
            application!!, ConnectivityManager::class.java
        )
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    val topActivity = ActivityManager.getInstance().topActivity
                    if (topActivity !is LifecycleOwner) {
                        return
                    }
                    val lifecycleOwner = topActivity as LifecycleOwner
                    if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) {
                        return
                    }
                    ToastUtils.show(R.string.common_network_error)
                }
            })
        }

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