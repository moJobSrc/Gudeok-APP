package com.gudeok.gudeokapp.util;

import android.util.Log;

public class Dlog {

    static final String TAG = "GD";


    /**
     * Log Level Error
     **/
    public static final void e(String message) {
        if (App.Companion.getDEBUG()) Log.e(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Warning
     **/
    public static final void w(String message) {
        if (App.Companion.getDEBUG()) Log.w(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Information
     **/
    public static final void i(String message) {
        if (App.Companion.getDEBUG()) Log.i(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Debug
     **/
    public static final void d(String message) {
        if (App.Companion.getDEBUG()) Log.d(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Verbose
     **/
    public static final void v(String message) {
        if (App.Companion.getDEBUG()) Log.v(TAG, buildLogMsg(message));
    }


    public static String buildLogMsg(String message) {

        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

        StringBuilder sb = new StringBuilder();

        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append("]");
        sb.append(message);

        return sb.toString();

    }
}