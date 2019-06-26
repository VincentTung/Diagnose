package com.vincent.diagnose.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.io.File

object ShareUtil {


    fun shareImage(context: Context, imagePath: String?) {
        if (imagePath != null) {
            val intent = Intent(Intent.ACTION_SEND)
            val file = File(imagePath)
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Share screen shot")
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(chooser)
            }
        } else {
            Toast.makeText(context, "先截屏，再分享", Toast.LENGTH_SHORT).show()
        }
    }
}