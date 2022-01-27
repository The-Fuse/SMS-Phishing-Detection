package com.solvabit.phishingsmsdetector

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.solvabit.phishingsmsdetector.databinding.ActivityOnBoardingBinding
import android.content.DialogInterface
import android.net.Uri

import android.os.Build
import android.provider.Settings
import android.util.Log

import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

import com.google.android.material.snackbar.Snackbar


class OnBoarding : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private var PERMISSIONS_CODE = 200
    private val permission = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.askPermission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23 && shouldShowRequestPermissionRationale(
                    permission[0]
                )
            ) {
                // User selected the Never Ask Again Option Change settings in app settings manually
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Change Permissions in Settings")
                alertDialogBuilder
                    .setMessage(
                        """
                    
                    Click Settings to Manually Set
                    Permissions
                    """.trimIndent()
                    )
                    .setCancelable(false)
                    .setPositiveButton("SETTINGS",
                        DialogInterface.OnClickListener { dialog, id ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, 1000) // Comment 3.
                        })
                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    permission,
                    PERMISSIONS_CODE
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.

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