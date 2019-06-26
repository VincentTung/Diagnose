package com.vincent.diagnose

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.vincent.diagnose.api.IPApi
import com.vincent.diagnose.api.IPCallback
import com.vincent.diagnose.util.*
import com.vincent.diagnose.util.PermissionUtils.REQ_PERMISSION_LOCATION
import com.vincent.diagnose.util.PermissionUtils.REQ_PERMISSION_STORAGE
import kotlinx.android.synthetic.main.activity_diagnose.*
import java.util.concurrent.Executors


class DiagnoseActivity : Activity(), View.OnClickListener, IPCallback {


    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private var api: IPApi =  IPApi(this)
    private val batteryReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra("level", 0)
            tv_battery.text = "$level%"
            unregisterReceiver(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnose)
        iv_diagnose_back.setOnClickListener(this)
        iv_diagnose_refresh.setOnClickListener(this)
        iv_diagnose_shot.setOnClickListener(this)
        tv_gps.setOnClickListener(this)
        tv_location_permission.setOnClickListener(this)
        doDiagnose()
    }

    private fun doDiagnose() {
        singleThreadExecutor.submit(api)
        progressbar.visibility = View.VISIBLE
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        if (NetworkUtil.isConnected(this)) {
            var networkType = NetworkUtil.getNetworkType(this)
            tv_net_state.setText(R.string.valid_network)
            if (networkType == NetworkUtil.NetworkType.NETWORK_WIFI) {
                tv_net_type.text = NetworkUtil.getWifiName(this)
            } else {
                tv_net_type.text = NetworkUtil.getNetworkTypeName(networkType, this)
            }
        } else {
            tv_net_state.setText(R.string.invalid_network)
        }
        tv_phone_model.text = PhoneInfoUtil.getModel()
        tv_os_version.text = PhoneInfoUtil.getOSVersion()
        tv_system_process.text =
            "共有" + ProcessUtil.getAllBackgroundProcessesSize(this) + "个进程正在运行"

        tv_root.text = if (PhoneInfoUtil.isRooted()) "是" else "否"
        tv_app_version.text = AppUtil.getAppNameAndVersion(this)
        tv_gps.text = if (PhoneInfoUtil.isGPSEnabled(this)) "开启" else "关闭"
        tv_location_permission.text = if (PermissionUtils.hasLocationPermission(this)) "已获得" else "未获得"

        tv_manufactor.text = PhoneInfoUtil.getManufacturer()
        tv_sdcard.text = PhoneInfoUtil.getSDCardState()
        tv_internal_storage.text = PhoneInfoUtil.getRomSize()

        tv_memory_size.text = PhoneInfoUtil.getMemoryState(this)
        tv_screen.text = PhoneInfoUtil.getScreenSize(this) + " (" + PhoneInfoUtil.getScreenDpiLevel(this) + ")"


    }

    override fun onClick(v: View?) {
        when (v) {
            iv_diagnose_back -> finish()
            iv_diagnose_shot -> {

                if (PermissionUtils.hasStoragePermission(this)) {
                    progressbar.visibility = View.VISIBLE
                    ScreenUtil.screenShot(this, scrollview)
                    progressbar.visibility = View.INVISIBLE
                } else {
                    PermissionUtils.requestStoragePermission(this)
                }
            }
            iv_diagnose_refresh -> {
                doDiagnose()
            }
            tv_gps -> {
                SettingUtil.openLocationSetting(this)
            }
            tv_location_permission -> {
                if (!PermissionUtils.hasLocationPermission(this)) {
                    PermissionUtils.requestLocationPermission(this)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_PERMISSION_LOCATION) {
            if (!PermissionUtils.hasLocationPermission(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
            } else {
                tv_location_permission.text = "已获取"
            }
        } else if (requestCode == REQ_PERMISSION_STORAGE) {
            if (!PermissionUtils.hasStoragePermission(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
            } else {
                progressbar.visibility = View.VISIBLE
                ScreenUtil.screenShot(this, scrollview)
                progressbar.visibility = View.INVISIBLE
            }
        }
    }

    override fun showIP(ip: String) {

        runOnUiThread(Runnable() {
            run(){
                tv_ip.text = ip
                progressbar.visibility = View.INVISIBLE
            }
        })
    }

}
