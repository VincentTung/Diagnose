package com.vincent.diagnose.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager

object NetworkUtil {

    private const val TD_CDMA = "TD-SCDMA"
    private const val WCDMA = "WCDMA"
    private const val CDMA2000 = "CDMA2000"

    enum class NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    fun isConnected(context: Context): Boolean {
        var networkInfo: NetworkInfo? = getActiveNetworkInfo(context)
        return networkInfo != null && networkInfo.isConnected
    }

    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val connectMgr: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectMgr?.activeNetworkInfo

    }

    fun getWifiName(context: Context): String {
        val wifiMgr: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

        wifiMgr.isWifiEnabled.let {
            return wifiMgr.connectionInfo.ssid.replace("\"", "")
        }
        return ""
    }

    fun getNetworkType(context: Context): NetworkType {
        var netType: NetworkType = NetworkType.NETWORK_NO
        val info = getActiveNetworkInfo(context)
        if (info != null && info.isAvailable) {
            if (info.type == 1) {
                netType = NetworkType.NETWORK_WIFI
            } else if (info.type == 0) {
                when (info.subtype) {
                    1, 2, 4, 7, 11, 16 -> netType = NetworkType.NETWORK_2G
                    3, 5, 6, 8, 9, 10, 12, 14, 15, 17 -> netType = NetworkType.NETWORK_3G
                    13, 18 -> netType = NetworkType.NETWORK_4G
                    else -> {
                        val subtypeName = info.subtypeName
                        netType = if (!subtypeName.equals(TD_CDMA, ignoreCase = true) && !subtypeName.equals(
                                WCDMA,
                                ignoreCase = true
                            ) && !subtypeName.equals(CDMA2000, ignoreCase = true)
                        ) {
                            NetworkType.NETWORK_UNKNOWN
                        } else {
                            NetworkType.NETWORK_3G
                        }
                    }
                }
            } else {
                netType = NetworkType.NETWORK_UNKNOWN
            }
        }

        return netType
    }

    fun getNetworkTypeName(networkType: NetworkType, context: Context): String? {
        return when (networkType) {
            NetworkUtil.NetworkType.NETWORK_WIFI -> "WIFI"
            NetworkUtil.NetworkType.NETWORK_2G -> getOperatorName(context) + " 2G"
            NetworkUtil.NetworkType.NETWORK_3G -> getOperatorName(context) + " 3G"
            NetworkUtil.NetworkType.NETWORK_4G -> getOperatorName(context) + " 4G"
            else -> ""
        }
    }

    private fun getOperatorName(context: Context): String? {
        val telMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telMgr?.networkOperatorName
    }

}