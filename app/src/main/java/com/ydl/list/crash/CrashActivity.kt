package com.ydl.list.crash

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ydl.list.R
import java.util.regex.Pattern

class CrashActivity : AppCompatActivity() {

    companion object{

        private const val INTENT_KEY_IN_THROWABLE = "throwable"

        /** 系统包前缀列表  */
        private val SYSTEM_PACKAGE_PREFIX_LIST = arrayOf(
            "android",
            "com.android",
            "androidx",
            "com.google.android",
            "java",
            "javax",
            "dalvik",
            "kotlin"
        )

        /** 报错代码行数正则表达式  */
        private val CODE_REGEX = Pattern.compile("\\(\\w+\\.\\w+:\\d+\\)")

        fun start(application: Application, throwable: Throwable?) {
            if (throwable == null) {
                return
            }
            val intent = Intent(application, CrashActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_THROWABLE, throwable)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash)
    }
}