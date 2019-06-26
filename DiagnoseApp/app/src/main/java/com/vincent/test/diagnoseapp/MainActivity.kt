package com.vincent.test.diagnoseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vincent.diagnose.DiagnoseActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var intent = Intent()
        intent.setClass(this,DiagnoseActivity().javaClass)
        startActivity(intent)
    }
}
