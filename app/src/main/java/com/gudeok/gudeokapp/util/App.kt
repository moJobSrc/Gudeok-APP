package com.gudeok.gudeokapp.util

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class App: Application() {
    companion object {
        lateinit var prefs: Prefs
        var DEBUG: Boolean = false;
    }
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(this)
        DEBUG = isDebuggable(this)
    }

    /**
     * get Debug Mode
     *
     * @param context
     * @return
     */
    private fun isDebuggable(context: Context): Boolean {
        var debuggable = false
        val pm: PackageManager = context.getPackageManager()
        try {
            val appinfo: ApplicationInfo = pm.getApplicationInfo(context.getPackageName(), 0)
            debuggable = 0 != appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
            /* debuggable variable will remain false */
        }
        return debuggable
    }
}