package com.ydl.list.toast;


import com.hjq.toast.ToastUtils;
import com.hjq.toast.config.IToastInterceptor;
import com.ydl.list.app.AppConfig;
import com.ydl.list.utils.LogCustomUtils;


/**
 * 自定义 Toast 拦截器（用于追踪 Toast 调用的位置）
 */
public final class ToastLogInterceptor implements IToastInterceptor {

    @Override
    public boolean intercept(CharSequence text) {
        if (AppConfig.isLogEnable()) {
            // 获取调用的堆栈信息
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            // 跳过最前面两个堆栈
            for (int i = 2; stackTrace.length > 2 && i < stackTrace.length; i++) {
                // 获取代码行数
                int lineNumber = stackTrace[i].getLineNumber();
                // 获取类的全路径
                String className = stackTrace[i].getClassName();
                if (lineNumber <= 0 || className.startsWith(ToastUtils.class.getName()) ||
                        className.startsWith(ToastAction.class.getName())) {
                    continue;
                }

                LogCustomUtils.tag("ToastUtils");
                LogCustomUtils.i("(" + stackTrace[i].getFileName() + ":" + lineNumber + ") " + text.toString());
                break;
            }
        }
        return false;
    }
}