package com.vincent.diagnose.util

import android.app.ActivityManager
import android.content.Context

object ProcessUtil{
    fun getAllBackgroundProcessesSize(context: Context): Int {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return if (am == null) {
            0
        } else {
            val info = am.runningAppProcesses
            var size =0
            if (info != null) {
                val iterator = info.iterator()
                while (iterator.hasNext()) {
                    val aInfo = iterator.next() as ActivityManager.RunningAppProcessInfo
                    size += aInfo.pkgList.size
                }
            }
            size
        }
    }
}