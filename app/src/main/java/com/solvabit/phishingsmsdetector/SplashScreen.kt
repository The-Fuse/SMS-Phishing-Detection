package com.solvabit.phishingsmsdetector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SplashScreen : AppCompatActivity() {
    private var PERMISSIONS_CODE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkSmsPermission()
    }

    private fun checkSmsPermission() {
        val permission = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
        if (ContextCompat.checkSelfPermission(
                this,
                "android.permission.RECEIVE_SMS"
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                "android.permission.READ_SMS"
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                "android.permission.SEND_SMS"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // App has permissions to listen incoming SMS messages
            Log.d("adnan", "checkForSmsReceivePermissions: Allowed");
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // App don't have permissions to listen incoming SMS messages
            Log.d("adnan", "checkForSmsReceivePermissions: Denied");

            // Request permissions from user
            val intent = Intent(this, OnBoarding::class.java)
            startActivity(intent)
            finish()
        }
    }

}