package com.solvabit.phishingsmsdetector.screens.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.solvabit.phishingsmsdetector.api.PhishingService
import androidx.navigation.fragment.findNavController
import com.solvabit.phishingsmsdetector.R
import com.solvabit.phishingsmsdetector.databinding.FragmentHomeBinding
import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import retrofit2.Call
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val contentResolver = requireActivity().contentResolver
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null, null, null, null
        )
        val homeViewModelFactory = HomeViewModelFactory(requireContext(), cursor!!)
        viewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = HomeAdapter(HomeAdapterListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMessagesFragment(viewModel.getList(it.address)))
        })
        binding.recyclerViewSender.adapter = adapter

        createChannel(
            getString(R.string.phishing_notification_channel_id),
            getString(R.string.phishing_notification_channel_name)
        )

        return binding.root
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "This gives Phishing reports"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    private fun checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                "android.permission.RECEIVE_SMS"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // App has permissions to listen incoming SMS messages
            Log.d("adnan", "checkForSmsReceivePermissions: Allowed");
            viewModel.readSms()
        } else {
            // App don't have permissions to listen incoming SMS messages
            Log.d("adnan", "checkForSmsReceivePermissions: Denied");

            // Request permissions from user
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECEIVE_SMS),
                200
            );
        }
    }

}