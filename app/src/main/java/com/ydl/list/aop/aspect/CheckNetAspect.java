package com.ydl.list.aop.aspect;
import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.hjq.toast.ToastUtils;
import com.ydl.list.R;
import com.ydl.list.aop.actions.CheckNet;
import com.ydl.list.manager.ActivityManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author yudongliang
 * create time 2021-10-12
 * describe :
 */
@Aspect
public class CheckNetAspect {

    @Pointcut("execution(@com.ydl.list.aop.actions.CheckNet * *(..))")
    public void method() {
        Log.e("YUDL","CheckNetAspect  method  exe");
    }

    @Around("method() && @annotation(checkNet)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, CheckNet checkNet) throws Throwable {
        Log.e("YUDL","CheckNetAspect  aroundJoinPoint  exe");
        Application application = ActivityManager.getInstance().getApplication();
        if (application != null) {
            ConnectivityManager manager = ContextCompat.getSystemService(application, ConnectivityManager.class);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                // 判断网络是否连接
                if (info == null || !info.isConnected()) {
                    ToastUtils.show(R.string.common_network_hint);
                    return;
                }
            }
        }
        //执行原方法
        joinPoint.proceed();
    }
}
