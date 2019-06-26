package com.vincent.diagnose.util

import android.content.Context
import android.content.pm.PackageManager

object AppUtil {

    fun getAppNameAndVersion(context: Context): String? {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            return pi?.applicationInfo?.loadLabel(pm)?.toString() + " " + pi?.versionName
        } catch (ex: PackageManager.NameNotFoundException) {
            ex.printStackTrace()
            return ""
        }

    }


}