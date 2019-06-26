package com.vincent.diagnose.util

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionUtils {
    val REQ_PERMISSION_LOCATION = 0x88
    val REQ_PERMISSION_STORAGE = 0x89

    private fun isGranted(permission: String, context: Context): Boolean {
        return 0 == ContextCompat.checkSelfPermission(context, permission)
    }

    fun hasLocationPermission(context: Context): Boolean {
        return isGranted(
            ACCESS_FINE_LOCATION,
            context
        ) && isGranted(ACCESS_COARSE_LOCATION, context)
    }

    fun hasStoragePermission(context: Context): Boolean {
        return isGranted(WRITE_EXTERNAL_STORAGE, context)
    }

    fun requestLocationPermission(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
                REQ_PERMISSION_LOCATION
            )
        } else {

        }
    }

    fun requestStoragePermission(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE),
                REQ_PERMISSION_STORAGE
            )
        } else {

        }
    }
}