package com.solvabit.phishingsmsdetector

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(ContextCompat.checkSelfPermission(baseContext, "android.permission.RECEIVE_SMS") == PackageManager.PERMISSION_GRANTED) {
            // App has permissions to listen incoming SMS messages
            Log.d("adnan", "checkForSmsReceivePermissions: Allowed");
        } else {
            // App don't have permissions to listen incoming SMS messages
            Log.d("adnan", "checkForSmsReceivePermissions: Denied");

            // Request permissions from user
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), 200);
        }
    }
}

