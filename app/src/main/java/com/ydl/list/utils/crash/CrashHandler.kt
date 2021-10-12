package com.ydl.list.utils.crash

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Process
import com.ydl.list.app.AppConfig

/**
 * @author yudongliang
 * create time 2021-10-12
 * describe : Crash处理类
 */
class CrashHandler private constructor (private val application: Application) : Thread.UncaughtExceptionHandler {
    /** Crash 文件名  */
    private val CRASH_FILE_NAME = "crash_file"
    /** Crash 时间记录  */
    private val KEY_CRASH_TIME = "key_crash_time"
    /** 一分钟 */
    private val MINUTE = 60 * 1000
    /**  异常捕获处理 */
    private var mNextHandler: Thread.UncaughtExceptionHandler? = null

    init {
        mNextHandler = Thread.getDefaultUncaughtExceptionHandler()
        //请不要重复注册 Crash 监听
        check(javaClass.name != mNextHandler!!.javaClass.name) {
            "are you ok?"
        }
    }

    companion object{
        fun register(application: Application){
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler(application))
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val sharedPreferences =
            application.getSharedPreferences(CRASH_FILE_NAME, Context.MODE_PRIVATE)
        val lastCrashTime = sharedPreferences.getLong(KEY_CRASH_TIME, 0)
        val currentCrashTime = System.currentTimeMillis()
        //记录崩溃的时间以便下次进行比对
        sharedPreferences.edit().putLong(KEY_CRASH_TIME,currentCrashTime).apply()
        val deadCrash = currentCrashTime - lastCrashTime < 5 * MINUTE
        if (AppConfig.isDebug()){
            CrashActivity.start(application, throwable)
        }else{
            if (!deadCrash) {
                // 如果不是致命的异常就自动重启应用
                RestartActivity.start(application)
            }
        }

        // 不去触发系统的崩溃处理（com.android.internal.os.RuntimeInit$KillApplicationHandler）
        if (mNextHandler != null && !mNextHandler!!.javaClass.name.startsWith("com.android.internal.os")) {
            mNextHandler!!.uncaughtException(thread, throwable)
        }

        // 杀死进程（这个事应该是系统干的，但是它会多弹出一个崩溃对话框，所以需要我们自己手动杀死进程）
        Process.killProcess(Process.myPid())
        System.exit(10)




    }

}