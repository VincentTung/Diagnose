package com.vincent.diagnose.api

import android.text.TextUtils
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception


class IPApi(var callback:IPCallback):Runnable{
    val IP_URL ="http://pv.sohu.com/cityjson?ie=utf-8"

    fun getIPInfo(){

        val url = URL(IP_URL)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = false
        httpURLConnection.connectTimeout = 10000
        httpURLConnection.readTimeout = 1000
        httpURLConnection.connect()


        val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream, "utf-8"))
        var line =""
        val stringBuilder = StringBuilder()
        do {
            line = bufferedReader.readLine()
            if (!TextUtils.isEmpty(line)) {
                stringBuilder.append(line)
            }
            break

        } while (true)
        var response = stringBuilder.toString().trim { it <= ' ' }

        response = response.replace("var returnCitySN = ", "")
        try {
            var jsonObject: JSONObject = JSONObject(response)
            var ip = jsonObject.getString("cip")
            var city = jsonObject.getString("cname")
            if (callback != null) {
                callback.showIP(ip + " " + city)
            }

        }catch ( e:Exception){
            callback.showIP("未获取")
        }
    }

    override fun run() {
        getIPInfo()
    }
}