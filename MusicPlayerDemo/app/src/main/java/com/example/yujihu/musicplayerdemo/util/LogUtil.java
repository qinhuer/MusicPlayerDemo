package com.example.yujihu.musicplayerdemo.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yujihu on 2016/5/31.
 */
public class LogUtil {
    private static final String tag = "mylog";
    private static final boolean isOpen = true;

    /**
     * 打印日志
     *
     * @param log
     */
    public static void log(String log) {
        if (isOpen) {
            Log.d(tag, log);
        }
    }

    /**
     * 显示toast
     *
     * @param context
     * @param message
     */
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
