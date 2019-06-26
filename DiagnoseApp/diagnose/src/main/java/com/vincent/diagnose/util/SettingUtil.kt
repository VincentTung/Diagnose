package com.vincent.diagnose.util

import android.content.Context
import android.content.Intent
import android.provider.Settings

object SettingUtil {
    fun openLocationSetting(context: Context) {
        val intent = Intent(
            Settings.ACTION_LOCATION_SOURCE_SETTINGS
        )
        context.startActivity(intent)
    }
}