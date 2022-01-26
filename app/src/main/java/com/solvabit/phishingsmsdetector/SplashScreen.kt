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
        setContentView(R.layout.activity_splash_screen)
        checkSmsPermission()
    }

    private fun checkSmsPermission() {
        val permission = arrayOf(Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS)
        if (ContextCompat.checkSelfPermission(this, "android.permission.RECEIVE_SMS") == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED)
        {
            // App has permissions to listen incoming SMS messages
            Log.d("adnan", "checkForSmsReceivePermissions: Allowed");
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        } else {
            // App don't have permissions to listen incoming SMS messages
            Log.d("adnan", "checkForSmsReceivePermissions: Denied");

            // Request permissions from user
            val intent = Intent(this,OnBoarding::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    val intent = Intent(this,OnBoarding::class.java)
                    startActivity(intent)

                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}