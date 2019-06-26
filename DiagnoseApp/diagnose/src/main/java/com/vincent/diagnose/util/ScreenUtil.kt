package com.vincent.diagnose.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.widget.ScrollView
import java.io.File
import java.io.FileOutputStream

object ScreenUtil {
    private fun shotScrollView(scrollView: ScrollView): Bitmap {
        var h = 0
        var bitmap: Bitmap? = null
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"))
        }
        bitmap = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap!!)
        scrollView.draw(canvas)
        return bitmap
    }

    fun screenShot(context: Context, view: ScrollView) {
        val imagePath = context.externalCacheDir!!.toString() + "/share.png"
        val bitmap = shotScrollView(view)
        val file = File(imagePath)
        if (file.exists()) {
            file.delete()
        }
        try {
            val os = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (file.exists()) {
                ShareUtil.shareImage(context, imagePath)
            }
        }
    }
}