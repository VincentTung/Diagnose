package com.vincent.diagnose.util

import android.app.ActivityManager
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.location.LocationManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.DisplayMetrics
import android.view.WindowManager
import java.io.File

object PhoneInfoUtil {
    private const val GPS = "gps"
    private const val HDPI = "hdpi"
    private const val XHDPI = "xhdpi"
    private const val XXHDPI = "xxhdpi"
    private const val XXXHDPI = "xxxhdpi"
    fun getModel(): String {
        var model: String? = Build.MODEL
        model = if (model != null) {
            model.trim { it <= ' ' }.replace("\\s*".toRegex(), "")
        } else {
            ""
        }
        return model
    }

    fun getOSVersion(): String? {
        return Build.VERSION.RELEASE
    }

    fun isRooted(): Boolean {
        val su = "su"
        val locations = arrayOf(
            "/system/bin/",
            "/system/xbin/",
            "/sbin/",
            "/system/sd/xbin/",
            "/system/bin/failsafe/",
            "/data/local/xbin/",
            "/data/local/bin/",
            "/data/local/"
        )
        val size = locations.size
        for (index in 0 until size) {
            val location = locations[index]
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

    fun isGPSEnabled(context: Context): Boolean {
        return (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(GPS)
    }

    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    fun getSDCardState(): String {
        val sdCardInfo = LongArray(2)
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            val bSize = sf.blockSize.toLong()
            val bCount = sf.blockCount.toLong()
            val availBlocks = sf.availableBlocks.toLong()
            sdCardInfo[0] = bSize * bCount
            sdCardInfo[1] = bSize * availBlocks
        }
        return "可用" + SizeConverter.convert(sdCardInfo[1]) + " / 共" + SizeConverter.convert(
            sdCardInfo[0]
        )
    }

    fun getRomSize(): String {
        val romInfo = LongArray(2)
        romInfo[0] = getTotalInternalMemorySize()
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSize.toLong()
        val availableBlocks = stat.availableBlocks.toLong()
        romInfo[1] = blockSize * availableBlocks

        return "可用" + SizeConverter.convert(romInfo[1]) + " / 共" + SizeConverter.convert(
            romInfo[0]
        )
    }


    private fun getTotalInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSize.toLong()
        val totalBlocks = stat.blockCount.toLong()
        return totalBlocks * blockSize
    }

    fun getMemoryState(context: Context): String {

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        return "可用" + SizeConverter.convert(mi.availMem) + " / 共" + SizeConverter.convert(
            mi.totalMem
        )
    }

    fun getScreenSize(context: Context): String? {
        val wm = context.getSystemService(WINDOW_SERVICE) as WindowManager
        if (wm == null) {
            return context.resources.displayMetrics.widthPixels.toString() + " x " + context.resources.displayMetrics.heightPixels.toString()
        } else {
            val point = Point()
            if (Build.VERSION.SDK_INT >= 17) {
                wm.defaultDisplay.getRealSize(point)
            } else {
                wm.defaultDisplay.getSize(point)
            }

            return point.x.toString() + " x " + point.y.toString()
        }
    }


    private  fun getScreenDensityDpi(context: Context): Int {
        return context.resources.displayMetrics.densityDpi
    }

    fun getScreenDpiLevel(context: Context): String {
        val dpi = getScreenDensityDpi(context)
        return if (dpi > DisplayMetrics.DENSITY_MEDIUM && dpi <= DisplayMetrics.DENSITY_HIGH) {
            HDPI
        } else if (dpi > DisplayMetrics.DENSITY_HIGH && dpi <= DisplayMetrics.DENSITY_XHIGH) {
            XHDPI
        } else if (dpi > DisplayMetrics.DENSITY_XHIGH && dpi <= DisplayMetrics.DENSITY_XXHIGH) {
            XXHDPI
        } else if (dpi > DisplayMetrics.DENSITY_XXHIGH && dpi <= DisplayMetrics.DENSITY_XXXHIGH) {
            XXXHDPI
        } else {
            ""
        }
    }
}