package com.ydl.list.aop.aspect;

import android.app.Activity;

import com.hjq.permissions.XXPermissions;
import com.tencent.bugly.crashreport.CrashReport;
import com.ydl.list.aop.actions.Permissions;
import com.ydl.list.manager.ActivityManager;
import com.ydl.list.utils.LogCustomUtils;
import com.ydl.list.utils.PermissionCallback;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;


/**
 * 权限申请切面
 */
@Aspect
public class PermissionsAspect {

    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.ydl.list.aop.actions.Permissions * *(..))")
    public void method() {}

    /**
     * 在连接点进行方法替换
     */
    @Around("method() && @annotation(permissions)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, Permissions permissions) {
        Activity activity = null;

        // 方法参数值集合
        Object[] parameterValues = joinPoint.getArgs();
        for (Object arg : parameterValues) {
            if (!(arg instanceof Activity)) {
                continue;
            }
            activity = (Activity) arg;
            break;
        }

        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            activity = ActivityManager.getInstance().getTopActivity();
        }

        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            LogCustomUtils.e("The activity has been destroyed and permission requests cannot be made");
            return;
        }

        requestPermissions(joinPoint, activity, permissions.value());
    }

    private void requestPermissions(ProceedingJoinPoint joinPoint, Activity activity, String[] permissions) {
        XXPermissions.with(activity)
                .permission(permissions)
                .request(new PermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            try {
                                // 获得权限，执行原方法
                                joinPoint.proceed();
                            } catch (Throwable e) {
                                CrashReport.postCatchedException(e);
                            }
                        }
                    }
                });
    }
}