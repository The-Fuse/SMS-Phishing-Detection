package com.solvabit.phishingsmsdetector.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.solvabit.phishingsmsdetector.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val contentResolver = requireActivity().contentResolver
        val homeViewModelFactory = HomeViewModelFactory(contentResolver)
        viewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

        viewModel.msgList.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.recyclerViewSender.adapter = MessageAdapter(it)
            }
        })



        displaySmsLog()


        return binding.root
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

    private fun displaySmsLog() {
        val allMessages: Uri = Uri.parse("content://sms/")
        //Cursor cursor = managedQuery(allMessages, null, null, null, null); Both are same
        val cursor = activity?.contentResolver?.query(allMessages, null, null, null, null) ?: return

        while (cursor.moveToNext()) {
            for (i in 0 until cursor.getColumnCount()) {
                Log.d(cursor.getColumnName(i).toString() + "", cursor.getString(i).toString() + "")
            }
            Log.d(
                "One row finished",
                "**************************************************"
            )
        }
    }

}